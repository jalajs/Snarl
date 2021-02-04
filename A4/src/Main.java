import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    // pars args
    String ipAddress = "127.0.0.1";
    String port = "8000";
    String name = "Glorifrir Flintshoulder";
    if (args.length == 3) {
      ipAddress = args[0];
      port = args[1];
      name = args[2];
    } else if (args.length == 2) {
      ipAddress = args[0];
      port = args[1];
    } else if (args.length == 1) {
      ipAddress = args[0];
    }

    try {
      ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port), 0, InetAddress.getByName(ipAddress));
      Socket socket = serverSocket.accept();
      Scanner serverScanner = new Scanner(socket.getInputStream());
      OutputStream output = socket.getOutputStream();
      PrintWriter clientWriter = new PrintWriter(output, true);

      SocketChannel channel = socket.getChannel();

      Scanner userScanner = new Scanner(System.in);

      sendUserNameVerification(name);

      int commandNumber = 0;
      while (userScanner.hasNextLine()) {
        List<JSONObject> actions = new ArrayList<>();
        String commands = userScanner.nextLine();

        JSONTokener tokener = new JSONTokener(commands.trim());
        JSONObject currentJSONCommand = new JSONObject();

        // parse out commands
        while (tokener.more()) {
          Object value = tokener.nextValue();
          String valueString = value.toString();
          currentJSONCommand = new JSONObject(valueString);
          actions.add(currentJSONCommand);
        }

        if (isNotWellFormedRequest(currentJSONCommand)) {
          JSONObject errorObject = new JSONObject();
          errorObject.put("error", "not a request");
          errorObject.put("object", currentJSONCommand);
          System.out.print(errorObject);
        }
        else if (commandNumber == 0) {
          try {
            JSONObject executable = createRoadsCommand(actions.get(0));
            clientWriter.print(executable);
            commandNumber++;
          } catch (IllegalArgumentException e) {
            JSONObject errorObject = new JSONObject();

            errorObject.put("error", "first command must be roads, please try again");
            errorObject.put("object", currentJSONCommand);
            System.out.print(errorObject);
          }
        }
        else if (isEndOfBatch(currentJSONCommand)){
          JSONObject executable = createBatchCommands(actions);
          // send executable to server
          clientWriter.print(executable);

          String serverResponse = serverScanner.nextLine();

          String character = (String) currentJSONCommand.get("character");
          String destination = (String) currentJSONCommand.get("town");

          JSONArray userOutputJSONArray = createUserOuputFromBatchResponse(serverResponse, character, destination);

          System.out.print(userOutputJSONArray);
          commandNumber++;
        }
      }

      userScanner.close();
      serverScanner.close();
      socket.close();
      serverSocket.close();

    } catch (IOException e) {
      System.out.print(e);
    }
  }

  // send the user name verification message
  public static void sendUserNameVerification(String name) {
    JSONArray returnJSONArray = new JSONArray();
    returnJSONArray.put("the server will call me");
    returnJSONArray.put("name");

    System.out.print(returnJSONArray);
  }

  // return true if command is well-formed
  public static boolean isNotWellFormedRequest(JSONObject commandJSONObject) {
    Object params = commandJSONObject.get("params");
    String command = (String) commandJSONObject.get("command");
    if (command.equals("roads")) {
      if (!(params instanceof JSONArray)) {
        return false;
      }
      JSONArray paramsArray = (JSONArray) params;
      for(int i = 0; i < paramsArray.length(); i++) {
        Object object = paramsArray.get(i);
        if (!(object instanceof JSONObject)) {
          return false;
        }
        JSONObject jsonObject = (JSONObject) object;
        return !jsonObject.has("to") || !jsonObject.has("from");
      }
    }
    else if (command.equals("place") || command.equals("passage-safe?")) {
      if (!(params instanceof JSONObject)) {
        return false;
      }
      JSONObject paramsObject= (JSONObject) params;
      return !paramsObject.has("character") || !paramsObject.has("town");
    }

    return false;
  }

  // create the response to send to the user after executing a batch
  public static JSONArray createUserOuputFromBatchResponse(String responseString, String character, String destination) {
    JSONObject responseObject = new JSONObject(responseString);
    JSONArray returnJSONArray = new JSONArray();

    JSONArray invalidPlacements = (JSONArray) responseObject.get("invalid");

    for (int i = 0; i < invalidPlacements.length(); i++) {
      JSONArray singleInvalidPlacement = new JSONArray();
      singleInvalidPlacement.put("invalid placement");
      singleInvalidPlacement.put(invalidPlacements.get(i));
      returnJSONArray.put(singleInvalidPlacement);
    }

    JSONArray passageSafeResponse = new JSONArray();
    JSONObject characterDestinationObject = new JSONObject();

    characterDestinationObject.put("character", character);
    characterDestinationObject.put("destination", destination);

    passageSafeResponse.put("the response for");
    passageSafeResponse.put(characterDestinationObject);
    passageSafeResponse.put("is");
    passageSafeResponse.put(responseObject.get("response"));

    returnJSONArray.put(passageSafeResponse);

    return returnJSONArray;
  }

  // check if command indicates end of batch
  public static boolean isEndOfBatch(JSONObject currentObject) {
    String command =  (String) currentObject.get("command");
    return command.equals("passage-safe?");
  }

  // create the JSONObject containing the information to establish the roads network
  public static JSONObject createRoadsCommand(JSONObject action) throws IllegalArgumentException {
    String command =  (String) action.get("command");
    if(command.equals("roads")){
      JSONArray params = (JSONArray) action.get("params");
      List<String> townNameRawStrings = new ArrayList<>();
      JSONArray roadArray = params;
      JSONArray townArray = new JSONArray();

      // check if towns are not in town array and add them
      for (int j = 0; j < params.length(); j++) {
        JSONObject curObj = (JSONObject) params.get(j);
        String toName = (String) curObj.get("to");
        String fromName = (String) curObj.get("from");

        if (!townNameRawStrings.contains(toName)) {
          townNameRawStrings.add(toName);
          townArray.put(toName);
        }
        if (!townNameRawStrings.contains(fromName)) {
          townNameRawStrings.add(fromName);
          townArray.put(fromName);
        }
      }
      JSONObject returnObject = new JSONObject();
      returnObject.put("towns", townArray);
      returnObject.put("roads", roadArray);
      return returnObject;
    } else {
      throw new IllegalArgumentException("command must be roads");
    }
  }

  // create the JSON object with all the batch information formatted to send to the server
  public static JSONObject createBatchCommands(List<JSONObject> actions) {
    JSONObject returnJSONObject = new JSONObject();
    JSONArray characterArray = new JSONArray();
    JSONObject queryObject = new JSONObject();

    for(int i = 0; i < actions.size() - 1; i++) {
      JSONObject obj = actions.get(i);
      String command =  (String) obj.get("command");

      if(command.equals("place")){
        JSONObject paramObj = (JSONObject) obj.get("params");
        String characterName = (String) paramObj.get("character");
        String townName = (String) paramObj.get("town");

        JSONObject placeCharacterObject = new JSONObject();
        placeCharacterObject.put("town", townName);
        placeCharacterObject.put("name", characterName);

        characterArray.put(placeCharacterObject);
      }
      if(command.equals("passage-safe?")){
        JSONObject paramObj = (JSONObject) obj.get("params");
        String characterName = (String) paramObj.get("character");
        String townName = (String) paramObj.get("town");

        queryObject.put("character", characterName);
        queryObject.put("town", townName);
      }
    }
    returnJSONObject.put("characters", characterArray);
    returnJSONObject.put("query", queryObject);
    return returnJSONObject;
  }
}
