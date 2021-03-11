import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Room;
import GameObjects.Tile;
import GameState.GameState;
import GameState.GameStateModel;

public class testState {
  private static testLevel testLevel;
  private static testRoom testRoom;

  /**
   * This method serves to take in json with [(state), (name), (point)] and returns an updated state
   * or an error message if the player cannot be placed in the destination.
   * <p>
   * where a state looks like: { "type": "state", "level": (level), "players":
   * (actor-position-list), "adversaries": (actor-position-list), "exit-locked": (boolean) } and a
   * actor-position-list looks like: [{ "type": (actor-type), "name": (string), "position": (point)
   * }, ...] a name is a JSONString and a point is a JSONArray containing two points Please see the
   * outputs types under generateOutput()
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    String tokenerSource = "";
    while (scanner.hasNextLine()) {
      String next = scanner.nextLine();
      tokenerSource += next + ' ';
    }
    scanner.close();

    JSONArray outputArray = new JSONArray();
    JSONTokener tokener = new JSONTokener(tokenerSource.trim());

    // parse out commands
    while (tokener.more()) {
      Object value = tokener.nextValue();
      String valueString = value.toString();
      // this has three items, the state JSON, the player(user) name, and the [ x, y] point
      JSONArray stateJSON = new JSONArray(valueString);

      JSONObject jsonState = (JSONObject) stateJSON.get(0);
      JSONString jsonName = (JSONString) stateJSON.get(1);
      JSONArray jsonPoint = (JSONArray) stateJSON.get(2);

      JSONObject jsonLevel = (JSONObject) jsonState.get("level");
      JSONArray jsonPlayerPosList = (JSONArray) jsonState.get("players");

      JSONArray jsonAdversaryPosList = (JSONArray) jsonState.get("adversaries");

      Boolean jsonExit = (Boolean) jsonState.get("exit-locked");

      JSONArray jsonRooms = (JSONArray) jsonLevel.get("rooms");
      JSONArray jsonHallways = (JSONArray) jsonLevel.get("hallways");
      JSONArray jsonObjects = (JSONArray) jsonLevel.get("objects");

      List<Room> rooms = testLevel.parseRooms(jsonRooms);
      List<Hallway> hallways = testLevel.parseHallways(jsonHallways);
      List<Posn> objectPositions = testLevel.parseObjects(jsonObjects);
      List<Actor> players = parseActorPositionList(jsonPlayerPosList);
      List<Actor> adversaries = parseActorPositionList(jsonAdversaryPosList);


      Posn point = testRoom.jsonToPosn(jsonPoint);

      Level level = testLevel.buildLevel(rooms, hallways, objectPositions);
      boolean traversable = level.checkTraversable(point);

      if (doesPlayerExist(jsonName.toString(), players)) {
        outputArray.put("Failure");
        outputArray.put("Player ");
        outputArray.put(jsonName);
        outputArray.put(" is not part of the game.");
      } else if (!traversable) {
        outputArray.put("Failure");
        outputArray.put("The destination position ");
        outputArray.put(jsonPoint);
        outputArray.put(" is invalid.");
      } else {
        GameState gs = buildGameState(level, players, adversaries);
        outputArray = generateOutput(gs, players, jsonExit, jsonLevel, jsonName, point);
      }
    }

    // print the output
    System.out.print(outputArray);

  }

  /**
   * Returns true if a player by the given name is in the list of players
   *
   * @param name
   * @param players
   * @return
   */
  private static boolean doesPlayerExist(String name, List<Actor> players) {
    for (Actor actor : players) {
      Player player = (Player) actor;
      if (name.equals(player.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * This helper method locates the player in a list of actors
   *
   * @param name
   * @param players
   * @return
   */
  private static Player findPlayer(String name, List<Actor> players) {
    for (Actor actor : players) {
      Player player = (Player) actor;
      if (name.equals(player.getName())) {
        return player;
      }
    }
    return null;
  }

  /**
   * This method generates output conditions assuming the player exists and the destination tile is
   * traversable. The possible outcomes are: [ "Success", (state) ] -> the position is unoccupied [
   * "Success", "Player ", (name), " was ejected.", (state) ] -> there is an adversary on the
   * position ["Success", "Player ", (name), " exited.", (state) ] -> the position is on the exit
   * and it is exitable
   *
   * @param gs
   * @param name
   * @param point
   * @return
   */
  private static JSONArray generateOutput(GameState gs, List<Actor> players, boolean exitable,
                                          JSONObject levelObject, JSONString name, Posn point) {
    JSONArray outputArray = new JSONArray();
    Player player = findPlayer(name.toString(), players);
    String interactionType = gs.handleMovePlayer(player, point);
    if (gs.isPlayerIsOnExit() && exitable) {
      outputArray.put("Success");
      outputArray.put("Player ");
      outputArray.put(name);
      outputArray.put(" exited.");
      outputArray.put(gameStateToJSONObject(gs, levelObject));
    }
    else if (interactionType.equals("None") || interactionType.equals("Key")) {
      outputArray.put("Success");
      outputArray.put(gameStateToJSONObject(gs, levelObject));

    } else if (interactionType.equals("Adversary")) {
      outputArray.put("Success");
      outputArray.put("Player ");
      outputArray.put(name);
      outputArray.put(" was ejected.");
      outputArray.put(gameStateToJSONObject(gs, levelObject));
    }

    return outputArray;
  }

  /**
   * Given a gameState, returns a JSON gameState object, which looks like this
   * { "type": "state",
   *   "level": (level),
   *   "players": (actor-position-list),
   *   "adversaries": (actor-position-list),
   *   "exit-locked": (boolean) }
   *
   * @param gs
   * @return
   */
  private static JSONObject gameStateToJSONObject(GameState gs, JSONObject levelObject) {
    JSONObject gameStateObject = new JSONObject();
    gameStateObject.put("type", "state");
    gameStateObject.put("level", levelObject);
    gameStateObject.put("players", actorListToJSONArray(gs.getActors(), "player"));
    gameStateObject.put("adversaries", actorListToJSONArray(gs.getActors(), "adversary"));
    gameStateObject.put("exit-locked", gs.isExitable());

    return gameStateObject;
  }

  /**
   * Turns the given list of actors into a actor-position list
   *  [{ "type": (actor-type), "name": (string), "position": (point) }, ...]
   * @param actors the list of actors we want to convert to a JSON list
   * @return actor-position-list JSONArray
   */
  private static JSONArray actorListToJSONArray(List<Actor> actors, String actorType) {
    JSONArray actorPositionList = new JSONArray();
    for (Actor actor : actors) {
      JSONObject actorPositionObject = new JSONObject();
      if (actor.isPlayer() && actorType.equals("player")) {
        Player player = (Player) actor;
        actorPositionObject.put("type","player");
        actorPositionObject.put("name", player.getName());
        actorPositionObject.put("position", player.getPosition());

      } else {
        Adversary adversary = (Adversary) actor;
        actorPositionObject.put("type", adversary.getType());
        actorPositionObject.put("name", adversary.getName());
        actorPositionObject.put("position", adversary.getPosition());
      }
      actorPositionList.put(actorPositionObject);
    }

    return actorPositionList;
  }



  /**
   * Parses an actor-position-list into Actor objects. An actor-position-list looks like [{ "type":
   * (actor-type), "name": (string), "position": (point) }, ...]
   *
   * @param actorPositionList
   * @return
   */
  private static List<Actor> parseActorPositionList(JSONArray actorPositionList) {
    List<Actor> actors = new ArrayList<>();
    for (int i = 0; i < actorPositionList.length(); i++) {
      JSONObject actorPositionObject = (JSONObject) actorPositionList.get(i);
      Actor actor = parseActor(actorPositionObject);
      actors.add(actor);
    }
    return actors;
  }

  /**
   * This method constructs an individual actor from a given actor-position.
   *
   * @param actorPositionObject
   * @return
   */
  private static Actor parseActor(JSONObject actorPositionObject) {
    String type = (String) actorPositionObject.get("type");
    String name = (String) actorPositionObject.get("name");
    Posn position = testRoom.jsonToPosn((JSONArray) actorPositionObject.get("position"));
    if (type.equals("player")) {
      Actor player = new Player(name);
      player.setPosition(position);
      return player;
    }
    Actor adversary = new Adversary(type, name);
    adversary.setPosition(position);
    return adversary;
  }

  /**
   * Constructs a GameState given a level, list of players, and list of adversaries.
   *
   * @param level       the given level parsed from the JSON input
   * @param players     the given list of actors parsed from the JSON state object
   * @param adversaries the give list of adversaries parsed from the JSON state object
   * @return A initialized GameState with the given data to be used to test the state.
   */
  private static GameState buildGameState(Level level, List<Actor> players, List<Actor> adversaries) {
    Posn keyPosition = level.getExitKeyPosition();
    GameState gs = new GameStateModel(level);
    gs.initGameState(players, adversaries, keyPosition);
    return gs;
  }
}
