import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GameObjects.Collectable;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Room;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Door;
import GameObjects.Level;

public class testLevel {
  private static testRoom testRoom;

  /**
   * [{ "type": "level",
   *   "rooms": [ { "type": "room",
   *                "origin": [ 3, 1 ],
   *                "bounds": { "rows": 4, "columns": 4 },
   *                "layout": [ [ 0, 0, 2, 0 ],
   *                            [ 0, 1, 1, 0 ],
   *                            [ 0, 1, 1, 0 ],
   *                            [ 0, 2, 0, 0 ] ] },
   *              { "type": "room",
   *                "origin": [ 10, 5 ],
   *                "bounds": { "rows": 5, "columns": 5 },
   *                "layout": [ [ 0, 0, 0, 0, 0 ],
   *                            [ 0, 1, 1, 1, 0 ],
   *                            [ 2, 1, 1, 1, 0 ],
   *                            [ 0, 1, 1, 1, 0 ],
   *                            [ 0, 0, 0, 0, 0 ] ] },
   *              { "type": "room",
   *                "origin": [ 4, 14 ],
   *                "bounds": { "rows": 5, "columns": 5 },
   *                "layout": [ [ 0, 0, 2, 0, 0 ],
   *                            [ 0, 1, 1, 1, 0 ],
   *                            [ 0, 1, 1, 1, 0 ],
   *                            [ 0, 1, 1, 1, 0 ],
   *                            [ 0, 0, 0, 0, 0 ] ] } ],
   *   "objects": [ { "type": "key", "position": [ 4, 2 ] },
   *                { "type": "exit", "position": [ 7, 17 ] } ],
   *   "hallways": [ { "type": "hallway",
   *                   "from": [ 3, 3 ],
   *                   "to": [ 4, 16 ],
   *                   "waypoints": [ [ 1, 3 ], [ 1, 16 ] ] },
   *                 { "type": "hallway",
   *                   "from": [ 6, 2 ],
   *                   "to": [ 12, 5 ],
   *                   "waypoints": [ [ 12, 2 ] ] } ]
   * }, [12, 4]]
   */


  /**
   * This method serves to take in json representations of level and a player position and print out
   * if the tile is traversable, its object, type, and reachable rooms from the room/hallway it
   * resides in
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

    JSONObject outputObject = new JSONObject();
    JSONTokener tokener = new JSONTokener(tokenerSource.trim());


    // parse out commands
    while (tokener.more()) {
      Object value = tokener.nextValue();
      String valueString = value.toString();
      // this has two items, the room JSON and the [ x, y] point
      JSONArray levelPointJSON = new JSONArray(valueString);

      JSONObject jsonLevel = (JSONObject) levelPointJSON.get(0);
      JSONArray jsonPoint = (JSONArray) levelPointJSON.get(1);

      JSONArray jsonRooms = (JSONArray) jsonLevel.get("rooms");
      JSONArray jsonHallways = (JSONArray) jsonLevel.get("hallways");
      JSONArray jsonObjects = (JSONArray) jsonLevel.get("objects");

      List<Room> rooms = parseRooms(jsonRooms);
      List<Hallway> hallways = parseHallways(jsonHallways);
      List<Posn> objectPositions = parseObjects(jsonObjects);
      Posn point = testRoom.jsonToPosn(jsonPoint);

      Level level = buildLevel(rooms, hallways, objectPositions);
      System.out.println(level.createLevelString());
      boolean traversable = level.checkTraversable(point);
      String objectType = level.checkObjectType(point);
      String levelSegType = level.checkSegmentType(point);

      JSONArray jsonReachable = new JSONArray();

      if (!levelSegType.equals("void")) {
          List<Posn> reachable = level.checkReachable(point, levelSegType);
          jsonReachable = testRoom.posnsToJson(reachable);
      }

      outputObject.put("traversable", traversable);
      outputObject.put("object", objectType);
      outputObject.put("type", levelSegType);
      outputObject.put("reachable", jsonReachable);

    }

    // print the output
    System.out.print(outputObject);

  }

  /**
   * Builds a level from a given list of rooms, hallways, and posns.
   *
   * @param rooms List<Rooms> indicates the level's rooms
   * @param hallways List<Hallway> indicates the level's hallways
   * @param exitAndKeyPosns List<Posn> includes the exit and the key position (ordered as listed)
   * @return the level
   */
  private static Level buildLevel(List<Room> rooms,
                                  List<Hallway> hallways,
                                  List<Posn> exitAndKeyPosns) {
    Level level = new Level(rooms, hallways, exitAndKeyPosns);
    return level;
  }

  /**
   * Parses the JSONArray of rooms into a list of Rooms
   * @param jsonRooms the given JSONArray representation of rooms
   * @return a list of rooms
   */
  private static List<Room> parseRooms(JSONArray jsonRooms) {
    List<Room> rooms = new ArrayList<>();
    for (int i = 0; i < jsonRooms.length(); i++) {
      JSONObject roomObject = (JSONObject) jsonRooms.get(i);
      Room room = testRoom.jsonToRoom(roomObject);
      rooms.add(room);
    }
    return rooms;
  }

  /**
   * Parses JSONArray of hallways into a list of hallways
   * @param jsonHallways the given JSONArray representation of hallways
   * @return a list of hallways
   */
  private static List<Hallway> parseHallways(JSONArray jsonHallways) {
    List<Hallway> hallways = new ArrayList();
    for (int i = 0; i < jsonHallways.length(); i++) {
      JSONObject hallwayObject = (JSONObject) jsonHallways.get(i);
      Hallway hallway = jsonToHallway(hallwayObject);
      hallways.add(hallway);
    }

    return hallways;
  }


  /**
   * Converts one hallway JSON object into a hallway
   * @param hallwayObject the given JSON hallway
   * @return a hallway built from the data provided in the JSON
   */
  private static Hallway jsonToHallway(JSONObject hallwayObject) {
    Hallway hallway = new Hallway();
    Posn from = testRoom.jsonToPosn((JSONArray) hallwayObject.get("from"));
    Posn to = testRoom.jsonToPosn((JSONArray) hallwayObject.get("to"));
    JSONArray waypointJsonArray = (JSONArray) hallwayObject.get("waypoints");

    List<Posn> waypoints = new ArrayList();
    for (int i = 0; i < waypointJsonArray.length(); i++) {
      Posn waypoint = testRoom.jsonToPosn((JSONArray) waypointJsonArray.get(i));
      waypoints.add(waypoint);
    }

    List<ArrayList<Tile>> segments = createSegments(to, from, waypoints);

    List<Door> doors = new ArrayList();
    Door fromDoor = new Door();
    Door toDoor = new Door();
    fromDoor.setTileCoord(from);
    toDoor.setTileCoord(to);
    doors.add(fromDoor);
    doors.add(toDoor);

    hallway.setDoors(doors);
    hallway.setWaypoints(waypoints);
    hallway.setTileSegments(segments);

    return hallway;
  }

  private static List<ArrayList<Tile>> createSegments(Posn to, Posn from, List<Posn> waypoints) {
    List<ArrayList<Tile>> segments = new ArrayList<>();
    List<Posn> allPoints = new ArrayList<>(waypoints);
    allPoints.add(0,from);
    allPoints.add(to);

    for (int i = 0; i < allPoints.size() - 1; i++) {
      ArrayList<Tile> tileSegement = new ArrayList<>();
      Posn start = allPoints.get(i);
      Posn end = allPoints.get(i + 1);

      if (start.getX() == end.getX()) {
        int range = Math.abs(start.getY() - end.getY());
        for (int t = 0; t < range - 1; t++) {
          Tile tile = new Tile(false);
          tileSegement.add(tile);
        }
      } else if (start.getY() == end.getY()) {
        int range = Math.abs(start.getX() - end.getX());
        for (int t = 0; t < range - 1; t++) {
          Tile tile = new Tile(false);
          tileSegement.add(tile);
        }
      }
      segments.add(tileSegement);
    }

    return segments;
  }

  /**
   * Parses the JSONArray of objects into a list of positions for the key and exit
   * @param jsonObjects the JSONArray for the positions of the key and exit
   * @return A list of posn for the locations of the key and exit. Key position is first and exit
   * position is second.
   */
  private static List<Posn> parseObjects(JSONArray jsonObjects) {
    List<Posn> positions = new ArrayList<>();
    JSONObject objectKey = (JSONObject) jsonObjects.get(0);
    JSONObject objectExit = (JSONObject) jsonObjects.get(1);

    Posn keyPosition = testRoom.jsonToPosn((JSONArray) objectKey.get("position"));
    Posn exitPosition = testRoom.jsonToPosn((JSONArray) objectExit.get("position"));

    positions.add(keyPosition);
    positions.add(exitPosition);

    return positions;
  }

}
