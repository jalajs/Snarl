import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

  public static void main(String[] args) {
    // parse args
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

      Scanner userScanner = new Scanner(System.in);

      sendUserNameVerification(name);
      // send name to server and receive sessionID
      clientWriter.println(name);
      String sessionId = serverScanner.nextLine();

      int commandNumber = 0;
      List<JSONObject> actions = new ArrayList<>();
      while (userScanner.hasNextLine()) {
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

        if (!isWellFormedRequest(currentJSONCommand)) {
          printErrorJSONObjectToUser("not a request", currentJSONCommand);
          actions.remove(actions.size() - 1);

        }

        else if (commandNumber == 0) {
          try {
            JSONObject executable = createRoadsCommand(actions.get(0));
            clientWriter.println(executable);
            commandNumber++;
            actions.clear();
          } catch (Exception e) {
            printErrorJSONObjectToUser("first command must be roads, please try again", currentJSONCommand);
          }
        }
        else if (isEndOfBatch(currentJSONCommand)){
          JSONObject executable = createBatchCommands(actions);
          actions.clear();
          // send executable to server
          clientWriter.println(executable);

          String serverResponse = serverScanner.nextLine();

          JSONObject paramObject = (JSONObject) currentJSONCommand.get("params");
          String character = (String) paramObject.get("character");
          String destination = (String) paramObject.get("town");

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
  private static void sendUserNameVerification(String name) {
    JSONArray returnJSONArray = new JSONArray();
    returnJSONArray.put("the server will call me");
    returnJSONArray.put(name);

    System.out.print(returnJSONArray);
  }

  // create and print the error JSONObject to the user
  private static void printErrorJSONObjectToUser(String message, JSONObject object) {
    JSONObject errorObject = new JSONObject();
    errorObject.put("error", message);
    errorObject.put("object", object);
    System.out.print(errorObject);
  }

  // return true if command is well-formed
  private static boolean isWellFormedRequest(JSONObject commandJSONObject) {
    try {
      Object params = commandJSONObject.get("params");
      String command = (String) commandJSONObject.get("command");
      if (command.equals("roads")) {
        JSONArray paramsArray = (JSONArray) params;
        for (int i = 0; i < paramsArray.length(); i++) {
          Object object = paramsArray.get(i);
          if (!(object instanceof JSONObject)) {
            return false;
          }
          JSONObject jsonObject = (JSONObject) object;
          return jsonObject.has("to") && jsonObject.has("from");
        }
      } else if (command.equals("place") || command.equals("passage-safe?")) {
        JSONObject paramsObject = (JSONObject) params;
        return paramsObject.has("character") && paramsObject.has("town");
      }
      return false;
    } catch (JSONException e) {
      return false;
    }
  }

  // create the response to send to the user after executing a batch
  private static JSONArray createUserOuputFromBatchResponse(String responseString, String character, String destination) {
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

    return passageSafeResponse;
  }

  // check if command indicates end of batch
  private static boolean isEndOfBatch(JSONObject currentObject) {
    String command =  (String) currentObject.get("command");
    return command.equals("passage-safe?");
  }

  // create the JSONObject containing the information to establish the roads network
  private static JSONObject createRoadsCommand(JSONObject action) throws IllegalArgumentException {
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
  private static JSONObject createBatchCommands(List<JSONObject> actions) {
    JSONObject returnJSONObject = new JSONObject();
    JSONArray characterArray = new JSONArray();
    JSONObject queryObject = new JSONObject();

    for(int i = 0; i < actions.size(); i++) {
      JSONObject obj = actions.get(i);
      String command =  (String) obj.get("command");

      JSONObject paramObj = (JSONObject) obj.get("params");
      String characterName = (String) paramObj.get("character");
      String townName = (String) paramObj.get("town");
      if(command.equals("place")){
        JSONObject placeCharacterObject = new JSONObject();
        placeCharacterObject.put("town", townName);
        placeCharacterObject.put("name", characterName);

        characterArray.put(placeCharacterObject);
      }
      if(command.equals("passage-safe?")){
        queryObject.put("character", characterName);
        queryObject.put("town", townName);
      }
    }
    returnJSONObject.put("characters", characterArray);
    returnJSONObject.put("query", queryObject);
    return returnJSONObject;
  }
}