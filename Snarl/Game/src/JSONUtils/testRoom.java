package JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Scanner;

import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Room;
import GameObjects.Tile;


public class testRoom {

  /**
   * This method serves to take in json representations of rooms and a player position and print out
   * the valid cardinal moves from the players position.
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

    JSONArray roomPointJSON = new JSONArray();

    // parse out commands
    while (tokener.more()) {
      Object value = tokener.nextValue();
      String valueString = value.toString();
      // this has two items, the room JSON and the [ row, col] point
      roomPointJSON = new JSONArray(valueString);

      JSONObject jsonRoom = (JSONObject) roomPointJSON.get(0);
      JSONArray jsonRoomOrigin = (JSONArray) jsonRoom.get("origin");
      JSONArray jsonPosn = (JSONArray) roomPointJSON.get(1);

      Room room = jsonToRoom(jsonRoom);
      Posn posn = jsonToPosn(jsonPosn);

      boolean success = room.isPosnInRoom(posn);

      if (!success) {
        outputArray.put("Failure: Point ");
        outputArray.put(jsonPosn);
        outputArray.put(" is not in room at ");
        outputArray.put(jsonRoomOrigin);
      } else {
        // find traversable points
        List<Posn> traversablePoints = room.getNextPossibleCardinalMoves(posn);
        JSONArray jsonTraversablePoints = posnsToJson(traversablePoints);

        outputArray.put("Success: Traversable points from ");
        outputArray.put(jsonPosn);
        outputArray.put(" in room at");
        outputArray.put(jsonRoomOrigin);
        outputArray.put(" are ");
        outputArray.put(jsonTraversablePoints);
      }
    }

    System.out.print(outputArray);
  }

  /**
   * Creates a JSONArray from the list of traversable points
   *
   * @param traversablePoints list of posns containing the traversable points
   * @return JSONArray containing another JSONArray for every point
   */
  public static JSONArray posnsToJson(List<Posn> traversablePoints) {
    JSONArray finalList = new JSONArray();
    for (Posn posn : traversablePoints) {
      JSONArray pointList = posnToJson(posn);
      finalList.put(pointList);
    }

    return finalList;
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
   * Converts the given JSONObject into the room.
   *
   * @param roomJSON looks like: { "type" : "room", "origin" : [0, 1], "bounds" : { "rows" : 3,
   *                 "columns" : 5 }, "layout" : [ [0, 0, 2, 0, 0], [0, 1, 1, 1, 0], [0, 0, 2, 0, 0]
   *                 ] }
   * @return the room built from the given object
   */
  public static Room jsonToRoom(JSONObject roomJSON) {
    Room room = new Room();
    Posn origin = jsonToPosn((JSONArray) roomJSON.get("origin"));
    JSONObject roomBounds = (JSONObject) roomJSON.get("bounds");
    int rows = (int) roomBounds.get("rows");
    int columns = (int) roomBounds.get("columns");
    Tile[][] tileGrid = jsonArrayToTileGrid((JSONArray) roomJSON.get("layout"), room);
    room.setUpperLeft(origin);
    room.setRows(rows);
    room.setCols(columns);
    room.setTileGrid(tileGrid);
    return room;
  }

  /**
   * Returns the tile grid specified by the given layout. The layout looks like this: [0, 0, 2, 0,
   * 0], [0, 1, 1, 1, 0], [0, 0, 2, 0, 0] where  2 = door, 1 = traversable tile, 0 = wall,
   *
   * @param layout the JSON array object
   * @return the tile grid built from layout
   */
  public static Tile[][] jsonArrayToTileGrid(JSONArray layout, Room room) {
    JSONArray firstRow = (JSONArray) layout.get(0);
    Tile[][] tileGrid = new Tile[layout.length()][firstRow.length()];
    for (int i = 0; i < layout.length(); i++) {
      JSONArray layoutRow = (JSONArray) layout.get(i);
      for (int j = 0; j < layoutRow.length(); j++) {
        int tileCode = (int) layoutRow.get(j);
        if (tileCode == 0) {
          Tile tile = new Tile(true);
          tileGrid[i][j] = tile;
        } else if (tileCode == 1) {
          Tile tile = new Tile(false);
          tileGrid[i][j] = tile;
        } else if (tileCode == 2) {
          Tile tile = new Tile(false);
          Door door = new Door();
          door.setTileCoord(new Posn(i, j));
          tile.setDoor(door);
          tileGrid[i][j] = tile;
        }
      }
    }
    return tileGrid;
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
}
