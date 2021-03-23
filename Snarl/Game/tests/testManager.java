import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GameManager.GameManagerClass;
import GameManager.GameManager;
import GameObjects.Actor;
import GameObjects.Door;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Room;

import User.User;

public class testManager {
  private static testLevel testLevel;
  private static testRoom testRoom;
  private static testState testState;


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
      // this has 5 items - the name list, level, number of turns, point list, and actor move list list
      JSONArray managerJSON = new JSONArray(valueString);

      JSONArray nameListJSON = (JSONArray) managerJSON.get(0);
      JSONObject levelJSON = (JSONObject) managerJSON.get(1);
      int maxRounds = (int) managerJSON.get(2);
      JSONArray pointListJSON = (JSONArray) managerJSON.get(3);
      JSONArray actorMoveListListJSON = (JSONArray) managerJSON.get(4);

      ArrayList<String> nameList = parseNameList(nameListJSON);
      Level level = parseLevelJSON(levelJSON);
      ArrayList<Posn> pointList = parsePointList(pointListJSON);
      ArrayList<ArrayList<Posn>> actorMoveListList = parseActorMoveListList(actorMoveListListJSON);


      // register n players from nameList
      GameManager gameManager = registerPlayers(nameList);

      // populate the given level with players and adversaries (locations for them given in pointList)
      gameManager.startGameTrace(level, pointList);

      JSONArray output = new JSONArray();
      JSONArray managerTrace = new JSONArray();

      // issue initial update to each player in order
     addUpdateTracesForGivenPlayers(managerTrace, nameList, gameManager);

      // play out game using moves from actorMoveList for each player.
      // validate and perform moves
      int roundNumber = 0;
      gameManager.setMoveInput(actorMoveListList);

      while(roundNumber < maxRounds) {
        List<String> remainingPlayers = gameManager.getRemainingPlayers();
        // move must be played for every remaining player in every round
        for(int i = 0; i < remainingPlayers.size(); i ++) {
          JSONArray response = gameManager.playOutMove(); // this should also process interactions
          managerTrace.put(response);
          if (checkIfGameTraceIsDone(roundNumber)) {
            roundNumber = maxRounds;
            break;
          }
          addUpdateTracesForGivenPlayers(managerTrace, remainingPlayers, gameManager);
        }
        roundNumber++;
      }

      // stop and return the results when:
      //  - given number of turns was performed
      //  - one of the move input stream is exhausted
      //  - the level is over


    }


  }

  /**
   * The game is over if the level has exited or one of the move input streams is exhausted.
   * @return
   */
  public static boolean checkIfGameTraceIsDone(int roundNumber) {
    return true;
  }

  public static void addUpdateTracesForGivenPlayers(JSONArray managerTrace, List<String> players, GameManager gameManager) {
    for (String s : players) {
      managerTrace.put(createUpdateTrace(s, gameManager));
    }
  }

  /**
   * Creates the update trace for the player
   * @param name
   * @param gameManager
   * @return
   */
  private static JSONArray createUpdateTrace(String name, GameManager gameManager) {
    JSONArray playerUpdateTrace = new JSONArray();
    playerUpdateTrace.put(name);

    JSONObject playerUpdate = createPlayerUpdateObject(name, gameManager);
    playerUpdateTrace.put(playerUpdate);

    return playerUpdateTrace;
  }

  /**
   * Creates a player update JSONObject that looks like this from the given name and manager {
   * "type": "player-update", "layout": (tile-layout), "position": (point), "objects":
   * (object-list), "actors": (actor-position-list) }
   *
   * @param name        the name of the player
   * @param gameManager the gamemanager from which to query the information
   * @return
   */
  private static JSONObject createPlayerUpdateObject(String name, GameManager gameManager) {
    JSONObject playerUpdate = new JSONObject();
    playerUpdate.put("type", "player-update");
    User user = gameManager.getUserByString(name);

    List<List<Tile>> surroundings = user.getSurroundings();
    JSONArray layout = createTileLayout(surroundings);
    playerUpdate.put("layout", layout);

    Posn currentPosition = user.getCurrentPosition();
    JSONArray position = testRoom.posnToJson(currentPosition);
    playerUpdate.put("position", position);

    List<Object> objects = user.findObjects();
    JSONArray objectList = createObjectList(objects);
    playerUpdate.put("objects", objectList);

    List<Actor> actors = user.findActors();
    JSONArray actorPositionList = createActorPositionList(actors);
    playerUpdate.put("actors", actorPositionList);

    return playerUpdate;
  }

  /**
   * Creates an actor-position-list from a given list of actors. An example of an
   * actor-position-list is [ { type: "player", name: "Jolene", position: [2, 2] }, ... ]
   *
   * @param actors
   * @return
   */
  private static JSONArray createActorPositionList(List<Actor> actors) {
    JSONArray actorPostionList = new JSONArray();
    for (Actor actor : actors) {
      String actorType = actor.isPlayer() ? "player" : "adversary";
      JSONObject actorPositionListItem = testState.createActorPositionListItem(actor, actorType);
      actorPostionList.put(actorPositionListItem);
    }
    return actorPostionList;
  }

  /**
   * Creates an object-list from the given list of objects. The only objects we are looking for are
   * exits and keys. An example object list is [ { type: "key", position: [1, 1] }, { type: "exit",
   * position: [2,2] } ]
   *
   * @param objects list of objects in the surrounings
   * @return the JSONArray representing the object list
   */
  private static JSONArray createObjectList(List<Object> objects) {
    JSONArray objectList = new JSONArray();
    for (Object object : objects) {
      if (object instanceof Door) {
        JSONObject objectListItem = new JSONObject();
        Door exit = (Door) object;
        objectListItem.put("type", "exit");
        objectListItem.put("position", testRoom.posnToJson(exit.getTileCoord()));
        objectList.put(objectListItem);
      } else if (object instanceof ExitKey) {
        JSONObject objectListItem = new JSONObject();
        ExitKey key = (ExitKey) object;
        objectListItem.put("type", "key");
        objectListItem.put("position", testRoom.posnToJson(key.getLocation()));
        objectList.put(objectListItem);
      }
    }
    return objectList;
  }


  /**
   * Creates a JSONArray representation of the given tile grid where 0 = wall or void 1 =
   * traversable tile 2 = door
   *
   * @param tiles
   * @return
   */
  private static JSONArray createTileLayout(List<List<Tile>> tiles) {
    JSONArray layout = new JSONArray();
    for (int i = 0; i < tiles.size(); i++) {
      JSONArray row = new JSONArray();
      for (int j = 0; j < tiles.get(i).size(); j++) {
        if (tiles.get(i).get(j) != null) {
          Tile tile = tiles.get(i).get(j);
          if (tile.getDoor() != null) {
            row.put(2);
          } else if (tile.getisWall()) {
            row.put(0);
          } else {
            row.put(1);
          }
        } else {
          row.put(0);
        }
      }
      layout.put(row);
    }
    return layout;
  }


//  private static JSONArray createMoveTrace(String name, GameManager gameManager) {
//
//  }

  /**
   * Registers the players in the name list in order to a game manager.
   *
   * @param nameList the names of each player to be added
   * @return A new game manager with the players registered
   */
  private static GameManager registerPlayers(ArrayList<String> nameList) {
    GameManager gameManager = new GameManagerClass();
    for (String name : nameList) {
      gameManager.addPlayer(name);
    }
    return gameManager;
  }

  /**
   * Parses the give list of actor move lists to list of list of posns
   *
   * @param actorMoveListListJSON Contains a list of moves for each registered player, in the same
   *                              order as they were listed in the name-list. Can assume this list
   *                              will always be equal to the length of the name-list
   * @return A list of list of posns
   */
  private static ArrayList<ArrayList<Posn>> parseActorMoveListList(JSONArray actorMoveListListJSON) {
    ArrayList<ArrayList<Posn>> actorMoveListList = new ArrayList<>();
    for (int i = 0; i < actorMoveListListJSON.length(); i++){
      JSONArray innerList = (JSONArray) actorMoveListListJSON.get(i);
      ArrayList<Posn> moveList = new ArrayList<>();
      for (int j = 0; j < innerList.length(); j++) {
        JSONObject moveObj = (JSONObject) innerList.get(j);
        Posn posn = testRoom.jsonToPosn((JSONArray) moveObj.get("to"));
        moveList.add(posn);
      }
      actorMoveListList.add(moveList);
    }
    return actorMoveListList;
  }

  /**
   * Parses the given point-list json to a list of posns
   *
   * @param pointListJSON the given list - has initial player and adversary positions. Given
   *                      positions are valid. The list will be at least n elements long, where n is
   *                      the number of players in the game. The first n elements are player
   *                      positions and any subsequent elements are adversaries.
   * @return The point-list parse to an arraylist of posn
   */
  private static ArrayList<Posn> parsePointList(JSONArray pointListJSON) {
    ArrayList<Posn> posnList = new ArrayList<>();
    for (int i = 0; i < pointListJSON.length(); i++) {
      JSONArray point = (JSONArray) pointListJSON.get(i);
      posnList.add(testRoom.jsonToPosn(point));
    }
    return posnList;
  }

  /**
   * Parses the given name-list JSON
   *
   * @param nameListJSON the name-list JSON
   * @return A list of player names. the order of the list determines the order of the players
   */
  private static ArrayList<String> parseNameList(JSONArray nameListJSON) {
    ArrayList<String> nameList = new ArrayList<>();
    for (int i = 0; i < nameListJSON.length(); i++) {
      nameList.add((String) nameListJSON.get(i));
    }
    return nameList;
  }

  /**
   * Parses the given JSON object into a level
   *
   * @param levelJSON a JSON representation of a level
   * @return the level object built from the JSON
   */
  private static Level parseLevelJSON(JSONObject levelJSON) {
    JSONArray jsonRooms = (JSONArray) levelJSON.get("rooms");
    JSONArray jsonHallways = (JSONArray) levelJSON.get("hallways");
    JSONArray jsonObjects = (JSONArray) levelJSON.get("objects");

    List<Room> rooms = testLevel.parseRooms(jsonRooms);
    List<Hallway> hallways = testLevel.parseHallways(jsonHallways);
    List<Posn> objectPositions = testLevel.parseObjects(jsonObjects);

    return testLevel.buildLevel(rooms, hallways, objectPositions);
  }
}
