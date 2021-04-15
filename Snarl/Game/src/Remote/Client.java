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
import java.util.List;
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

  public Client(String host, int port) throws IOException {
    this.socket = new Socket(InetAddress.getByName(host), port);
    this.user = new LocalUser();
    this.input = this.socket.getInputStream();
    this.reader = new BufferedReader(new InputStreamReader(input));
    this.output = this.socket.getOutputStream();
    this.writer = new PrintWriter(output, true);
    this.alive = true;
  }

  public void run() {
    // while loop that displays server message and sends what the user wants
    while (alive) {
      // process request from game server
      String serverData = receive();
      if (serverData.equals("null")) {
        try {
          socket.close();
        }
        catch (IOException e) {
          System.out.println(e);
        }
      }
      if (!serverData.equals("null") || !serverData.equals("")) {
        String response = executeServerRequest(serverData);
        if (!response.equals("{}")) {
          // send it to server
          send(response);
        }
      }
    }

  }

  private static String executeServerRequest(String serverData) {
    JSONObject response = new JSONObject();
    if (serverData.equals("name")) {
      return user.promptForName(userScanner);
    } else if (serverData.equals("move") || serverData.equals("Invalid")) {
      Action action = user.turn(userScanner);
      response.put("type", "move");
      response.put("to", utils.posnToJson(action.getDestination()));
      return response.toString();
    } else if (serverData.charAt(0) == '{') {
      JSONObject serverRequest = new JSONObject(serverData);
      String type = (String) serverRequest.get("type");
      switch (type) {
        case "welcome":
          String serverInfo = (String) serverRequest.get("info");
          System.out.println(serverInfo);
          break;
        case "start-level":
          int levelNum = (int) serverRequest.get("level");
          levelNum++;
          System.out.println("Level " + levelNum + " about to start");
          user.setExitable(false);
          break;
        case "player-update":
          Posn currentPosition = utils.jsonToPosn((JSONArray) serverRequest.get("position"));
          user.setCurrentPosition(currentPosition);

          List<List<Tile>> surroundings = utils.jsonToSurroundings(serverRequest);
          user.setSurroundings(surroundings);

          String message = (String) serverRequest.get("message");
          if (message.contains("key")) {
            user.setExitable(true);
          }
          System.out.println("game update: " + message);
          break;
        case "end-level":
          System.out.println("Level is over");
          break;
        case "end-game":
          try {
           System.out.println("The game is donzo");
           alive = false;
           socket.close();
          } catch (IOException e) {
            System.out.println("did not let the socket close on end game :(");
          }
          break;
        case "player-score":
          break;
        default:
          // throw an error?
          break;
      }
    }
      return response.toString();
    }

    private void send (String message){
      writer.println(message);
    }

    private String receive () {
      String message = "";
      try {
        message = message + reader.readLine();
      } catch (IOException e) {
        System.out.println(e);
      }
      return message;
    }
  }
