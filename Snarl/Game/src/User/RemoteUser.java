package User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Action.Action;
import Action.MoveAction;
import GameObjects.Actor;
import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Tile;
import JSONUtils.RemoteUtils;


public class RemoteUser implements User {
  private Socket socket;
  private String name;
  private Posn currentPosition;
  private List<List<Tile>> surroundings;
  private static InputStream input;
  private static BufferedReader reader;
  private static OutputStream output;
  private static PrintWriter writer;
  private static boolean isExitable;
  private int numExits;
  private int numEjects;
  private int numKeysCollected;
  private int totalNumExits;
  private int totalNumEjects;
  private int totalNumKeysCollected;
  private int hitPoints = 100;


  /**
   * This constructs a remote user with the given socket. To set a name, it prompts the client for
   * the name and sets it.
   */
  public RemoteUser(Socket socket) {
    this.socket = socket;
    this.numExits = 0;
    this.numEjects = 0;
    this.numKeysCollected = 0;
    this.totalNumExits = 0;
    this.totalNumEjects = 0;
    this.totalNumKeysCollected = 0;
    this.name = "";
    try {
      this.input = this.socket.getInputStream();
      this.reader = new BufferedReader(new InputStreamReader(input));
      this.output = this.socket.getOutputStream();
      this.writer = new PrintWriter(output, true);
    } catch (IOException e) {
      System.out.println("IO exception");
    }
  }

  /**
   * This method controls sending and recieving the name request from the socket.
   */
  public String promptName() {
    String nameRequest = "name";
    send(nameRequest);
    this.name = receive();
    return this.name;
  }

  /**
   * This method counts the number of times a name has been used before
   * @param existingNames
   * @return
   */
  private int numOfPrevNameUsages(List<String> existingNames) {
    int i = 0;
    for(String existingName : existingNames) {
      i++;
    }
    return i;
  }

  /**
   * Sends a message to client socket corresponding to this remote user
   *
   * @param message the message to be sent
   */
  public void send(String message) {
    try {
      if (message == null) {
        socket.close();
      }
    } catch (IOException e) {
      System.out.print("Unable to close socket with error: " + e);
    }
    try {
      this.output = this.socket.getOutputStream();
      this.writer = new PrintWriter(output, true);
      writer.println(message);
    } catch (IOException e) {
      System.out.print("Unable to write to client with message: " + e);
    }
  }

  /**
   * Receives messages from the client socket associated with this remote user
   *
   * @return the message recieved from the client
   */
  public String receive() {
    String message = "";
    try {
      this.input = this.socket.getInputStream();
      this.reader = new BufferedReader(new InputStreamReader(input));
      message = message + reader.readLine();
    } catch (IOException e) {
      System.out.println("Unable to receive message with error: " + e);
    }
    return message;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Posn getCurrentPosition() {
    return this.currentPosition;
  }

  @Override
  public List<List<Tile>> getSurroundings() {
    return surroundings;
  }

  @Override
  public void setSurroundings(List<List<Tile>> surroundings) {
    this.surroundings = surroundings;
  }

  @Override
  public void update(List<List<Tile>> updatedSurroundings, boolean isExitable, Posn currentPosition, List<String> remainingPlayers, String event) {
    this.currentPosition = currentPosition;
    this.surroundings = updatedSurroundings;
    this.isExitable = isExitable;

    JSONObject playerUpdate = new JSONObject();
    playerUpdate.put("type", "player-update");

    JSONArray layout = RemoteUtils.createTileLayout(surroundings);
    playerUpdate.put("layout", layout);

    JSONArray position = RemoteUtils.posnToJson(currentPosition);
    playerUpdate.put("position", position);

    List<Object> objects = this.findObjects();
    JSONArray objectList = RemoteUtils.createObjectList(objects);
    playerUpdate.put("objects", objectList);

    List<Actor> actors = this.findActors();
    JSONArray actorPositionList = RemoteUtils.createActorPositionList(actors, remainingPlayers);
    playerUpdate.put("actors", actorPositionList);

    playerUpdate.put("hitPoints", this.hitPoints);

    playerUpdate.put("message", event.equals("") ? null : event);

    this.send(playerUpdate.toString());
  }

  /**
   * Executes a players turn.
   *
   * @param scanner that we don't use here
   */
  @Override
  public Action turn(Scanner scanner) {
    MoveAction action = new MoveAction(this.currentPosition, this.currentPosition);
    this.send("move");
    String response = this.receive();
    JSONObject playerMove = new JSONObject(response);
    JSONArray maybePoint = (JSONArray) playerMove.get("to");
    if (maybePoint != null) {
      Posn destination = RemoteUtils.jsonToPosn(maybePoint);
      action = new MoveAction(destination, this.currentPosition);
    }
    return action;
  }

  @Override
  public List<Object> findObjects() {
    List<Object> objects = new ArrayList<>();
    for (int i = 0; i < this.surroundings.size(); i++) {
      for (int j = 0; j < this.surroundings.get(i).size(); j++) {
        if (this.surroundings.get(i).get(j) != null) {
          Tile tile = this.surroundings.get(i).get(j);
          Door door = tile.getDoor();
          if (tile.getCollectable() != null) {
            objects.add(tile.getCollectable());
          }
          if (door != null && door.isLevelExit()) {
            objects.add(door);
          }
        }
      }
    }
    return objects;
  }

  @Override
  public List<Actor> findActors() {
    List<Actor> actors = new ArrayList<>();
    for (int i = 0; i < this.surroundings.size(); i++) {
      for (int j = 0; j < this.surroundings.get(i).size(); j++) {
        if (surroundings.get(i).get(j) != null) {
          Tile tile = surroundings.get(i).get(j);
          Actor occupier = tile.getOccupier();
          if (occupier != null && !(i == 2 && j == 2)) {
            actors.add(occupier);
          }
        }
      }
    }
    return actors;
  }

  @Override
  public String visibleTileRepresentation() {
    return null;
  }

  @Override
  public void setCurrentPosition(Posn posn) {
    this.currentPosition = posn;
  }

  @Override
  public void setExitable(boolean exitable) {
    this.isExitable = exitable;
  }

  @Override
  public String promptForName(Scanner scanner) {
    return null;
  }

  public int getNumExits() {
    return this.numExits;
  }

  public void setNumExits(int numExits) {
    this.numExits = numExits;
  }

  public int getNumEjects() {
    return numEjects;
  }

  public void setNumEjects(int numEjects) {
    this.numEjects = numEjects;
  }

  public int getNumKeysCollected() {
    return numKeysCollected;
  }

  public void setNumKeysCollected(int numKeysCollected) {
    this.numKeysCollected = numKeysCollected;
  }

  @Override
  public int getTotalNumExits() {
    return this.totalNumExits;
  }

  @Override
  public int getTotalNumEjects() {
    return this.totalNumEjects;
  }

  @Override
  public int getTotalNumKeysCollected() {
    return this.totalNumKeysCollected;
  }

  /**
   * This method updates all the game level stats and the total stats using the given boolean
   * parameters.
   *
   * @param isEjected
   * @param isExited
   * @param isKeyFinder
   */
  @Override
  public void updateStats(boolean isEjected, boolean isExited, boolean isKeyFinder) {
    // update game level stats
    this.numEjects = isEjected ? this.numEjects + 1 : this.numEjects;
    this.numExits = isExited ? this.numExits + 1 : this.numExits;
    this.numKeysCollected = isKeyFinder ? this.numKeysCollected + 1 : this.numKeysCollected;
  }

  /**
   * This method updates the accumulative stats for this user using the game level stats
   */
  @Override
  public void updateLeaderBoardStats() {
    this.totalNumEjects +=  this.numEjects;
    this.totalNumExits += this.numExits;
    this.totalNumKeysCollected += this.numKeysCollected;
  }


  @Override
  public void renderView() {

  }

  public void subtractFromHitPoints(int damage) {
    this.hitPoints = this.hitPoints - damage;
  }

  @Override
  public void setHitPoints(int hitPoints) {
    this.hitPoints = hitPoints;
  }

  @Override
  public int getHitPoints() {
    return hitPoints;
  }
}
