package Remote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Action.Action;
import GameObjects.Posn;
import GameObjects.Tile;
import JSONUtils.RemoteUtils;
import User.User;
import User.LocalUser;

public class Client {
  private static Socket socket;
  private static User user;
  private static Scanner userScanner = new Scanner(System.in);
  private static RemoteUtils utils = new RemoteUtils();
  private static InputStream input;
  private static BufferedReader reader;
  private static OutputStream output;
  private static PrintWriter writer;
  private static boolean alive;
  private static Map<String, Integer> playerIdMap;

  /**
   * This constructs a basic client that owns a LocalUser
   *
   * @param host
   * @param port
   * @throws IOException
   */
  public Client(String host, int port) throws IOException {
    this.socket = new Socket(InetAddress.getByName(host), port);
    this.user = new LocalUser();
    this.input = this.socket.getInputStream();
    this.reader = new BufferedReader(new InputStreamReader(input));
    this.output = this.socket.getOutputStream();
    this.writer = new PrintWriter(output, true);
    this.alive = true;
    this.playerIdMap = new HashMap<>();
  }

  /**
   * Method that runs the client by continuously checking for messages from the server and sending
   * responses. If a null message is recieved, the client shuts down. If an empty message is
   * recieved, the client does nothing.
   */
  public void run() {
    while (alive) {
      String serverData = receive();
      if (serverData.equals("null") || (serverData.equals("invalid_name"))) {
        try {
          System.out.println("Closing connection, server data: " + serverData);
          alive = false;
          socket.close();
        } catch (IOException e) {
          System.out.println("Recevied null data from the server. Cannot close socket.\n" + e);
        }
      } else if (!serverData.equals("")) {
        String response = executeServerRequest(serverData);
        if (!response.equals("{}")) {
          send(response);
        }
      }
    }

  }

  /**
   * This method performs the server request. The protocol are specific.
   *
   * @param serverData
   * @return
   */
  private static String executeServerRequest(String serverData) {
    JSONObject response = new JSONObject();
    if (serverData.equals("name")) {
      return user.promptForName(userScanner);
    } else if (serverData.equals("move") || serverData.equals("Invalid")) {
      return promptMove(response);
    } else if (serverData.equals("terminate")){
      exitClient();
    } else if (serverData.charAt(0) == '{') {
      JSONObject serverRequest = new JSONObject(serverData);
      String type = (String) serverRequest.get("type");
      switch (type) {
        case "welcome":
          System.out.println((String) serverRequest.get("info"));
          break;
        case "start-level":
          displayStartLevel(serverRequest);
          break;
        case "player-update":
          displayUpdate(serverRequest);
          break;
        case "end-level":
          displayEndLevel(serverRequest);
          break;
        case "end-game":
          displayEndGame(serverRequest);
          break;
        default:
          System.out.println("Something mysterious is happening");
          break;
      }
    }
    return response.toString();
  }

  /**
   * Exits the client by closing the socket
   */
  private static void exitClient() {
    try {
      System.out.println("Thank you for playing snarl!");
      alive = false;
      socket.close();
    } catch (IOException e) {
      System.out.println("Could not close socket with exception " + e);
    }
  }

  /**
   * This method prompts the user for a move and builds the response
   *
   * @param response
   * @return
   */
  private static String promptMove(JSONObject response) {
    Action action = user.turn(userScanner);
    response.put("type", "move");
    response.put("to", utils.posnToJson(action.getDestination()));
    System.out.println();
    return response.toString();
  }

  /**
   * This method displays the end game method to the user
   *
   * @param serverRequest
   */
  private static void displayEndGame(JSONObject serverRequest) {
    System.out.println("The game is donezo! Thank you for playing SNARL.");
    printScores((JSONArray) serverRequest.get("scores"));
    // add scores from this game to leaderboard
    printLeaderboard((JSONArray) serverRequest.get("leaderboard"));
    // running leaderboard of players from the games finished so far
  }

  /**
   * Display the start level message to the user
   *
   * @param serverRequest
   */
  private static void displayStartLevel(JSONObject serverRequest) {
    int levelNum = (int) serverRequest.get("level");
    levelNum++;
    // map the active players in the game to a number
    playerIdMap = new HashMap<>();
    JSONArray nameList = (JSONArray) serverRequest.get("players");
    for (int i = 0; i < nameList.length(); i++) {
      String name = (String) nameList.get(i);
      int id = i + 1;
      System.out.println("Player " + name + " is represented as " + id);
      playerIdMap.put(name, id);
    }
    System.out.println("Level " + levelNum + " about to start");
    user.setExitable(false);
    user.setHitPoints(100);
    System.out.println();
  }

  /**
   * Displays the update message to user. This method also updates isExitable if the most recent
   * update message is about a key.
   *
   * @param serverRequest
   */
  private static void displayUpdate(JSONObject serverRequest) {
    Posn currentPosition = utils.jsonToPosn((JSONArray) serverRequest.get("position"));
    user.setCurrentPosition(currentPosition);

    List<List<Tile>> surroundings = utils.jsonToSurroundings(user.getName(), serverRequest, playerIdMap);
    user.setSurroundings(surroundings);

    int hitPoints = (int) serverRequest.get("hitPoints");
    user.setHitPoints(hitPoints <= 0 ? 0 : hitPoints);

    String hitPointMessage = user.getHitPoints() > 50 ? " You're doing great!" :
            user.getHitPoints() != 0 ? " Be careful!" : " You've been ejected :(";
    String message = (String) serverRequest.get("message");
    if (message.contains("key")) {
      user.setExitable(true);
    }
    user.renderView();
    System.out.println("Game update: " + message);
    System.out.println("You have " + user.getHitPoints() + " hit points left." + hitPointMessage);
    System.out.println();
  }


  /**
   * Displays the end level messages to the user.
   *
   * @param serverRequest the end-level request object sent from the server
   */
  private static void displayEndLevel(JSONObject serverRequest) {
    System.out.println("Level is over");
    JSONArray exits = (JSONArray) serverRequest.get("exits");
    JSONArray ejects = (JSONArray) serverRequest.get("ejects");
    String keyFinder = (String) serverRequest.get("key");
    System.out.println("Player " + keyFinder + " found the key");
    System.out.println("Player(s) exited: " + exits.toString().replace("]", "").replace("[", "").replace("\"", ""));
    System.out.println("Player(s) ejected: " + ejects.toString().replace("]", "").replace("[", "").replace("\"", ""));
    System.out.println("You ended this level with " + user.getHitPoints() + " hit points.");
    System.out.println();
  }


  /**
   * This method prints the score list passed to client. This method is called in the end game
   * scenario
   *
   * @param scoreList
   */
  private static void printScores(JSONArray scoreList) {
    System.out.println("Game specific stats:");
    for (int i = 0; i < scoreList.length(); i++) {
      JSONObject scoreObject = (JSONObject) scoreList.get(i);
      String name = (String) scoreObject.get("name");
      int exits = (int) scoreObject.get("exits");
      int ejects = (int) scoreObject.get("ejects");
      int keys = (int) scoreObject.get("keys");
      System.out.println(name + " exited " + exits + " time(s), was ejected " + ejects + " time(s), and found " + keys + " keys.");
    }
    System.out.println();
  }


  /**
   * This method prints the score list passed to client. This prints out the running leaderboard stats of
   * all games so far for each user
   *
   * @param scoreList
   */
  private static void printLeaderboard(JSONArray scoreList) {
    System.out.println("Leaderboard:");
    Object[][] leaderboard = new String[scoreList.length() + 1][];
    leaderboard[0] = new String[] {"name", "exits", "ejects", "keys"};
    for (int i = 0; i < scoreList.length(); i++) {
      JSONObject scoreObject = (JSONObject) scoreList.get(i);
      String name = (String) scoreObject.get("name");
      String exits = String.valueOf(scoreObject.get("exits"));
      String ejects = String.valueOf(scoreObject.get("ejects"));
      String keys = String.valueOf(scoreObject.get("keys"));
      leaderboard[i + 1] = new String[] { name, exits, ejects, keys};
    }

    for (Object[] row : leaderboard) {
      System.out.format("%15s%15s%15s%n", row);
    }
  }

  /**
   * This method writes a message to the server
   *
   * @param message
   */
  private void send(String message) {
    writer.println(message);
  }

  /**
   * This method receives a message from the server
   *
   * @return
   */
  private String receive() {
    String message = "";
    try {
      message = message + reader.readLine();
    } catch (IOException e) {
      System.out.println("Error thrown while receiving data: " + e);
    }
    return message;
  }
}
