package JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.AdversaryType;
import GameObjects.Door;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Room;
import GameObjects.Tile;

/**
 * Class that holds all the utils for the remote protocol
 */
public class RemoteUtils {

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
        String actorType = actor.isPlayer() ? "player" : ((Adversary) actor).getType().type;
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
   * list of tiles { "type": "player-update", "layout": (tile-layout), "position": (point),
   * "objects": (object-list), "actors": (actor-position-list) }
   *
   * @param playerUpdateObject the player update json object
   * @return
   */
  public List<List<Tile>> jsonToSurroundings(String playerName, JSONObject playerUpdateObject, Map<String, Integer> playerIdMap) {
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
        this.setTileInfo(playerName, tile, i, j, playerUpdateObject, playerIdMap);
      }
      surroundings.add(row);
    }
    return surroundings;
  }

  /**
   * This method sets the tile information based on the given params
   * @param playerName
   * @param tile the
   * @param row the row of the tile
   * @param col the col of the tile
   * @param playerUpdateObject
   * @param playerIdMap
   */
  private void setTileInfo(String playerName, Tile tile, int row, int col, JSONObject playerUpdateObject, Map<String, Integer> playerIdMap) {
    Posn playerCurrentPosition = jsonToPosn((JSONArray) playerUpdateObject.get("position"));
    JSONArray actorPositionList = (JSONArray) playerUpdateObject.get("actors");

    setTileOccupiersUsingActorPosnList(actorPositionList, tile, row, col, playerIdMap, playerCurrentPosition);
    JSONArray objectPositionList = (JSONArray) playerUpdateObject.get("objects");
    setTileObjectsUsingObjectList(objectPositionList, tile, row, col, playerCurrentPosition);

    if (row == 2 && col == 2) {
      setCurrentPlayer(playerName, tile, playerIdMap);
    }
  }

  /**
   * This method sets the current player to the given tile
   * @param playerName
   * @param tile
   * @param playerIdMap
   */
  private void setCurrentPlayer(String playerName, Tile tile, Map<String, Integer> playerIdMap) {
      int playerId = playerIdMap.get(playerName);
      tile.setOccupier(new Player(playerId));
  }

  /**
   * This method uses the given actor position list to set the give tiles occupiers
   * @param actorPositionList contains all nearby actors
   * @param tile the tile who needs an occupier
   * @param row
   * @param col
   * @param playerIdMap the map
   */
  private void setTileOccupiersUsingActorPosnList(JSONArray actorPositionList, Tile tile,
                                                  int row, int col, Map<String, Integer> playerIdMap,
  Posn playerCurrentPosition) {
    for (int i = 0; i < actorPositionList.length(); i++) {
      JSONObject actorPosnObj = (JSONObject) actorPositionList.get(i);
      Posn actorPosn = jsonToPosn((JSONArray) actorPosnObj.get("position"));
      Posn actorPosnRelToSurroundings = generatePosnRelativeToSurroundings(actorPosn.getRow(), actorPosn.getCol(), playerCurrentPosition);
      if (actorPosnRelToSurroundings.equals(new Posn(row, col))) {
        String type = (String) actorPosnObj.get("type");
        if (type.equals("player")) {
          String name = (String) actorPosnObj.get("name");
          int id = playerIdMap.get(name);
          tile.setOccupier(new Player(id));
        } else if (type.equalsIgnoreCase("Zombie")) {
          tile.setOccupier(new Adversary(AdversaryType.ZOMBIE, "Zombie"));
        } else if (type.equalsIgnoreCase("Ghost")) {
          tile.setOccupier(new Adversary(AdversaryType.GHOST, "Ghost"));
        }
      }
    }
  }

  /**
   * This method sets the objects in the tiles by position
   * @param objectPositionList
   * @param tile
   * @param row
   * @param col
   * @param playerCurrentPosition
   */
  private void setTileObjectsUsingObjectList(JSONArray objectPositionList, Tile tile, int row, int col, Posn playerCurrentPosition) {
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


  /**
   * Calculate the position relative to the surroundings (rather than rel to the level)
   *
   * @param levelRow
   * @param levelCol
   * @return
   */
  private Posn generatePosnRelativeToSurroundings(int levelRow, int levelCol, Posn currentPosition) {
    int x = 2 + (levelRow - currentPosition.getRow());
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
