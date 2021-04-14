package JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import GameManager.GameManager;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Collectable;
import GameObjects.Door;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Room;
import GameObjects.Tile;
import User.User;

public class RemoteUtils {
  private static testLevel testLevel;
  private static testRoom testRoom;
  private static testState testState;


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

    List<String> actorNames = gameManager.getRemainingPlayers();
    List<Actor> actors = user.findActors();
    JSONArray actorPositionList = createActorPositionList(actors, actorNames);
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
  public static JSONArray createActorPositionList(List<Actor> actors, List<String> actorNames) {
    JSONArray actorPostionList = new JSONArray();
    for (Actor actor : actors) {
      if (actorNames.contains(actor.getName()) || !actor.isPlayer()) {
        String actorType = actor.isPlayer() ? "player" : ((Adversary) actor).getType().toLowerCase();
        JSONObject actorPositionListItem = testState.createActorPositionListItem(actor, actorType);
        actorPostionList.put(actorPositionListItem);
      }
    }
    return actorPostionList;
  }


  /**
   * Creates an object-list from the given list of objects. The only objects we are looking for are
   * exits and keys. An example object list is [ { type: "key", position: [1, 1] }, { type: "exit",
   * position: [2,2] } ]
   *
   * @param objects list of objects in the surroundings
   * @return the JSONArray representing the object list
   */
  public static JSONArray createObjectList(List<Object> objects) {
    // todo: fix this weird object thing
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
  public static JSONArray createTileLayout(List<List<Tile>> tiles) {
    JSONArray layout = new JSONArray();
    for (int i = 0; i < tiles.size(); i++) {
      JSONArray row = new JSONArray();
      for (int j = 0; j < tiles.get(i).size(); j++) {
        if (tiles.get(i).get(j) != null) {
          Tile tile = tiles.get(i).get(j);
          if (tile.getDoor() != null) {
            row.put(2);
          } else if (tile.isWall()) {
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


  /**
   * Converts the given JSONArray for a point into a GameObjects.Posn.
   *
   * @param posnJSON looks like: [1, 3]
   * @return the posn built from the given object.
   */
  public static Posn jsonToPosn(JSONArray posnJSON) {
    int row = (int) posnJSON.get(0);
    int col = (int) posnJSON.get(1);
    return new Posn(row, col);
  }


  /**
   * This method takes in the JSON representation of a tile layout and converts it into a list of
   * list of tiles
   *{
   *   "type": "player-update",
   *   "layout": (tile-layout),
   *   "position": (point),
   *   "objects": (object-list),
   *   "actors": (actor-position-list)
   * }
   * @param playerUpdateObject the player update json object
   * @return
   */
  public List<List<Tile>> jsonToSurroundings(JSONObject playerUpdateObject) {
    JSONArray layout = (JSONArray) playerUpdateObject.get("layout");
    List<List<Tile>> surroundings = new ArrayList<>();
    for (int i = 0; i < layout.length(); i++) {
      JSONArray layoutRow = (JSONArray) layout.get(i);
      List<Tile> row = new ArrayList<>();
      Tile tile = new Tile();
      for (int j = 0; j < layoutRow.length(); j++) {
        int tileCode = (int) layoutRow.get(j);
        if (tileCode == 0) {
          tile = new Tile(true);
          row.add(tile);
        } else if (tileCode == 1) {
          tile = new Tile(false);
          row.add(tile);
        } else if (tileCode == 2) {
          tile = new Tile(false);
          Door door = new Door();
          door.setTileCoord(new Posn(i, j));
          tile.setDoor(door);
          row.add(tile);
        }
        this.setTileInfo(tile, i, j, playerUpdateObject);
      }
      surroundings.add(row);
    }
    return surroundings;
  }
  private void setTileInfo(Tile tile, int row, int col, JSONObject playerUpdateObject) {
    Posn playerCurrentPosition = jsonToPosn((JSONArray) playerUpdateObject.get("position"));
    if (row == 2 && col == 2) {
      tile.setOccupier(new Player());
    } else {
      JSONArray actorPositionList = (JSONArray) playerUpdateObject.get("actors");
      for (int i = 0; i < actorPositionList.length(); i++) {
        JSONObject actorPosnObj = (JSONObject) actorPositionList.get(i);
        Posn actorPosn = jsonToPosn((JSONArray) actorPosnObj.get("position"));
        Posn actorPosnRelToSurroundings = generatePosnRelativeToSurroundings(actorPosn.getRow(), actorPosn.getCol(), playerCurrentPosition);
        if (actorPosnRelToSurroundings.equals(new Posn(row, col))) {
          String type = (String) actorPosnObj.get("type");
          if (type.equals("player")) {
            tile.setOccupier(new Player());
          } else if (type.equals("Zombie")) {
            tile.setOccupier(new Adversary("Zombie", "Zombie"));
          } else if (type.equals("Ghost")) {
            tile.setOccupier(new Adversary("Ghost", "Ghost"));
          }
        }
      }
      JSONArray objectPositionList = (JSONArray) playerUpdateObject.get("objects");
      for (int i = 0; i < objectPositionList.length(); i++) {
        JSONObject objectPosnObj = (JSONObject) objectPositionList.get(i);
        String type = (String) objectPosnObj.get("type");
        Posn objectPosn = jsonToPosn((JSONArray) objectPosnObj.get("position"));
        Posn objectPosnRelToSurroundings = generatePosnRelativeToSurroundings(objectPosn.getRow(), objectPosn.getCol(), playerCurrentPosition);
        if (objectPosnRelToSurroundings.equals(new Posn(row, col))) {
          if (type.equals("key")) {
            tile.setCollectable(new ExitKey(new Posn(row, col)));
          } else if (type.equals("exit")) {
            Door exit = new Door();
            exit.setLevelExit(true);
            tile.setDoor(exit);
          }
        }
      }
    }
  }

  /**
   * Calculate the position relative to the surroundings (rather than rel to the level)
   * @param levelRow
   * @param levelCol
   * @return
   */
  private Posn generatePosnRelativeToSurroundings(int levelRow, int levelCol, Posn currentPosition) {
    int x = 2 + (levelRow- currentPosition.getRow());
    int y = 2 + (levelCol - currentPosition.getCol());
    return new Posn(x, y);
  }


  /**
   * Creates a JSONArray containing the coordinates for the given GameObjects.Posn
   *
   * @param point the given position
   * @return JSONArray containing the coordinates
   */
  public static JSONArray posnToJson(Posn point) {
    int row = point.getRow();
    int col = point.getCol();
    JSONArray pointList = new JSONArray();
    pointList.put(row);
    pointList.put(col);
    return pointList;
  }

  /**
   * Parse the given levels file
   *
   * @param levelValue the name of the levels file
   * @return a list of levels
   */
  public static List<Level> parseLevelsFile(String levelValue) {
    List<Level> levels = new ArrayList<>();
    try {
      File file = new File(levelValue);

      Scanner scanner = new Scanner(file);
      String tokenerSource = "";
      while (scanner.hasNextLine()) {
        String next = scanner.nextLine();
        tokenerSource += next + ' ';
      }
      scanner.close();

      JSONTokener tokener = new JSONTokener(tokenerSource.trim());
      buildLevels(tokener, levels);

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      System.out.println(levelValue + " is an invalid level file. Please try again.");
      System.exit(1);
    }
    return levels;
  }

  /**
   * This method mutates the given level List so that it contains all the parsed out levels
   * described in the JSONTokener
   *
   * @param tokener
   * @param levels
   */
  private static void buildLevels(JSONTokener tokener, List<Level> levels) {
    int levelNumber = (int) tokener.nextValue();
    // parse out commands
    while (tokener.more()) {
      Object value = tokener.nextValue();
      String valueString = value.toString();

      JSONObject jsonLevel = new JSONObject(valueString);
      JSONArray jsonRooms = (JSONArray) jsonLevel.get("rooms");
      JSONArray jsonHallways = (JSONArray) jsonLevel.get("hallways");
      JSONArray jsonObjects = (JSONArray) jsonLevel.get("objects");

      List<Room> rooms = testLevel.parseRooms(jsonRooms);
      List<Hallway> hallways = testLevel.parseHallways(jsonHallways);
      List<Posn> objectPositions = testLevel.parseObjects(jsonObjects);

      Level level = testLevel.buildLevel(rooms, hallways, objectPositions);
      levels.add(level);
    }
  }
}
