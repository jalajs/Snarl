import java.util.ArrayList;
import java.util.List;

/**
 * Contains utils methods for creating data so that the tests are more readable
 */
public class TestUtils {

  /**
   * Create a simple 2x2 room with no walls
   */
  public Room createSimpleRoom() {
    Room simpleRoom = new Room();
    List<ArrayList<Tile>> simpleTileGrid = new ArrayList<>();
    ArrayList<Tile> simpleTileRow = new ArrayList<>();
    simpleTileRow.add(new Tile(false));
    simpleTileRow.add(new Tile(false));
    simpleTileGrid.add(simpleTileRow);
    simpleTileGrid.add(simpleTileRow);
    simpleRoom.setTileGrid(simpleTileGrid);
    simpleRoom.setxDim(2);
    simpleRoom.setyDim(2);
    simpleRoom.setUpperLeft(new Posn(0, 0));

    return simpleRoom;
  }

  /**
   * Create a 3x3 room with wals
   */
  public Room createWalledRoom() {
    Room walledRoom = new Room();
    List<ArrayList<Tile>> walledTileGrid = new ArrayList<>();
    ArrayList<Tile> wallTileRow = new ArrayList<>();
    wallTileRow.add(new Tile(true));
    wallTileRow.add(new Tile(true));
    wallTileRow.add(new Tile(true));
    walledTileGrid.add(wallTileRow);
    walledTileGrid.add(wallTileRow);
    walledTileGrid.add(wallTileRow);
    walledRoom.setTileGrid(walledTileGrid);
    return walledRoom;
  }

  /**
   * Create a simple room with a door
   */
  public Room createSimpleRoomWithDoor() {
    Room roomWithDoor = new Room();
    roomWithDoor.setUpperLeft(new Posn(0, 0));
    List<ArrayList<Tile>> simpleRoomWithDoor = new ArrayList<>();
    ArrayList<Tile> bottomRow = new ArrayList<>();
    Tile doorTile = new Tile(false);
    Door door = new Door();
    doorTile.setDoor(door);
    bottomRow.add(new Tile(false));
    bottomRow.add(doorTile);

    ArrayList<Tile> simpleTileRow = new ArrayList<>();
    simpleTileRow.add(new Tile(false));
    simpleTileRow.add(new Tile(false));

    simpleRoomWithDoor.add(simpleTileRow);
    simpleRoomWithDoor.add(bottomRow);

    roomWithDoor.setTileGrid(simpleRoomWithDoor);

    List<Door> doorPos = new ArrayList<>();

    door.setTileCoord(new Posn(1, 1));
    doorPos.add(door);

    roomWithDoor.setDoorPositions(doorPos);

    roomWithDoor.setxDim(2);
    roomWithDoor.setyDim(2);

    return roomWithDoor;
  }

  /**
   * Create a complicated room with different kinds of tiles
   */
  public Room createComplicatedRoom() {
    Room complicatedRoom = new Room();
    ExitKey key = new ExitKey();
    Door door = new Door();

    List<Door> doorPos = new ArrayList<>();

    door.setTileCoord(new Posn(7, 6));
    doorPos.add(door);


    Actor player = new Player();
    Tile keyTile = new Tile(false);
    Tile playerTile = new Tile(false);
    Tile doorTile = new Tile(false);
    keyTile.setCollectable(key);
    doorTile.setDoor(door);

    playerTile.setOccupier(player);
    List<ArrayList<Tile>> complicatedTileGrid = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow1 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow2 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow3 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow4 = new ArrayList<>();
    complicatedTileRow1.add(keyTile);
    complicatedTileRow1.add(new Tile(false));
    complicatedTileRow1.add(new Tile(false));
    complicatedTileRow2.add(new Tile(true));
    complicatedTileRow2.add(new Tile(true));
    complicatedTileRow2.add(playerTile);
    complicatedTileRow3.add(doorTile);
    complicatedTileRow3.add(playerTile);
    complicatedTileRow3.add(new Tile(false));
    complicatedTileRow4.add(playerTile);
    complicatedTileRow4.add(new Tile(false));
    complicatedTileRow4.add(new Tile(true));
    complicatedTileGrid.add(complicatedTileRow1);
    complicatedTileGrid.add(complicatedTileRow2);
    complicatedTileGrid.add(complicatedTileRow3);
    complicatedTileGrid.add(complicatedTileRow4);
    complicatedRoom.setTileGrid(complicatedTileGrid);
    complicatedRoom.setUpperLeft(new Posn(5, 6));
    complicatedRoom.setxDim(4);
    complicatedRoom.setyDim(3);
    complicatedRoom.setDoorPositions(doorPos);

    return complicatedRoom;
  }

  /**
   * Create a hallway that connects two rooms with one waypoint
   */
  public Hallway createHallway() {
    Room roomWithDoor = createSimpleRoomWithDoor();
    Room complicatedRoom = createComplicatedRoom();

    Hallway hallway = new Hallway();
    List<Posn> waypoints = new ArrayList<>();
    waypoints.add(new Posn(1, 3));
    waypoints.add(new Posn(7, 3));
    hallway.setWaypoints(waypoints);

    List<ArrayList<Tile>> segments = new ArrayList<>();
    ArrayList<Tile> segment1 = new ArrayList<>();
    ArrayList<Tile> segment2 = new ArrayList<>();
    ArrayList<Tile> segment3 = new ArrayList<>();

    segment1.add(new Tile(false));

    segment2.add(new Tile(false));
    segment2.add(new Tile(false));
    segment2.add(new Tile(false));
    segment2.add(new Tile(false));
    segment2.add(new Tile(false));

    segment3.add(new Tile(false));
    segment3.add(new Tile(false));

    segments.add(segment1);
    segments.add(segment2);
    segments.add(segment3);

    List<Door> doors = new ArrayList<>();

    Door door1 = roomWithDoor.getDoorPositions().get(0);
    Door door2 = complicatedRoom.getDoorPositions().get(0);

    doors.add(door1);
    doors.add(door2);

    hallway.setDoors(doors);
    hallway.setTileSegments(segments);

    return hallway;
  }

  /**
   * Create a complicated level with many rooms and hallways
   */
  public Level createComplicatedLevel() {
    Level complicatedLevel = new Level(15, 10);
    List<Room> rooms = new ArrayList<>();
    List<Hallway> hallways = new ArrayList<>();

    Room room1 = new Room();
    List<ArrayList<Tile>> room1TileGrid = new ArrayList<>();
    ArrayList<Tile> room1Row1 = new ArrayList<>();
    ArrayList<Tile> room1Row2 = new ArrayList<>();
    ArrayList<Tile> room1Row3 = new ArrayList<>();
    Door room1Door = new Door();
    Tile room1DoorTile = new Tile();
    room1Door.setTileCoord(new Posn(2,2));
    room1DoorTile.setDoor(room1Door);

    List<Door> room1DoorPos = new ArrayList<>();
    room1DoorPos.add(room1Door);

    room1Row1.add(new Tile(false));
    room1Row1.add(new Tile(false));
    room1Row1.add(new Tile(false));

    room1Row2.add(new Tile(false));
    room1Row2.add(new Tile(false));
    room1Row2.add(new Tile(true));

    room1Row3.add(new Tile(false));
    room1Row3.add(new Tile(false));
    room1Row3.add(room1DoorTile);

    room1TileGrid.add(room1Row1);
    room1TileGrid.add(room1Row2);
    room1TileGrid.add(room1Row3);

    room1.setDoorPositions(room1DoorPos);
    room1.setTileGrid(room1TileGrid);
    room1.setUpperLeft(new Posn(0,0));
    room1.setxDim(3);
    room1.setyDim(3);

    Room room2 = new Room();
    List<ArrayList<Tile>> room2TileGrid = new ArrayList<>();
    ArrayList<Tile> room2Row1 = new ArrayList<>();
    ArrayList<Tile> room2Row2 = new ArrayList<>();
    ArrayList<Tile> room2Row3 = new ArrayList<>();
    Door room2Door1 = new Door();
    Door room2Door2 = new Door();

    room2Door1.setTileCoord(new Posn(2,6));
    room2Door2.setTileCoord(new Posn(2,9));

    Tile room2DoorTile1 = new Tile();
    Tile room2DoorTile2 = new Tile();
    room2DoorTile1.setDoor(room2Door1);
    room2DoorTile2.setDoor(room2Door2);

    List<Door> room2DoorPos = new ArrayList<>();
    room2DoorPos.add(room2Door1);
    room2DoorPos.add(room2Door2);

    room2Row1.add(new Tile(false));
    room2Row1.add(new Tile(false));
    room2Row1.add(new Tile(false));

    room2Row2.add(new Tile(false));
    room2Row2.add(new Tile(false));
    room2Row2.add(new Tile(false));

    room2Row3.add(room2DoorTile1);
    room2Row3.add(new Tile(false));
    room2Row3.add(room2DoorTile2);

    room2TileGrid.add(room2Row1);
    room2TileGrid.add(room2Row2);
    room2TileGrid.add(room2Row3);

    room2.setDoorPositions(room2DoorPos);
    room2.setTileGrid(room2TileGrid);
    room2.setUpperLeft(new Posn(0,7));
    room2.setxDim(3);
    room2.setyDim(3);

    Room room3 = new Room();

    List<ArrayList<Tile>> room3TileGrid = new ArrayList<>();
    Tile room3DoorTile1 = new Tile();
    Tile room3DoorTile2 = new Tile();

    Door room3d1 = new Door();
    Door room3d2 = new Door();
    room3d1.setTileCoord(new Posn(6, 3));
    room3d2.setTileCoord(new Posn(9, 3));

    room3DoorTile1.setDoor(room3d1);
    room3DoorTile2.setDoor(room3d2);

    List<Door> room3doorPos = new ArrayList<>();
    room3doorPos.add(room3d1);
    room3doorPos.add(room3d2);

    ArrayList<Tile> room3row1 = new ArrayList<>();
    ArrayList<Tile> room3row2 = new ArrayList<>();
    ArrayList<Tile> room3row3 = new ArrayList<>();
    ArrayList<Tile> room3row4 = new ArrayList<>();

    room3row1.add(new Tile(false));
    room3row1.add(new Tile(false));
    room3row1.add(new Tile(false));
    room3row1.add(room3DoorTile1);


    room3row2.add(new Tile(true));
    room3row2.add(new Tile(true));
    room3row2.add(new Tile(false));
    room3row2.add(new Tile(true));

    room3row3.add(new Tile(false));
    room3row3.add(new Tile(false));
    room3row3.add(new Tile(false));
    room3row3.add(new Tile(false));

    room3row4.add(new Tile(false));
    room3row4.add(new Tile(false));
    room3row4.add(new Tile(false));
    room3row4.add(room3DoorTile2);


    room3TileGrid.add(room3row1);
    room3TileGrid.add(room3row2);
    room3TileGrid.add(room3row3);
    room3TileGrid.add(room3row4);

    room3.setDoorPositions(room3doorPos);
    room3.setUpperLeft(new Posn(6, 0));
    room3.setTileGrid(room3TileGrid);
    room3.setxDim(4);
    room3.setyDim(4);

    Room room4 = new Room();

    List<ArrayList<Tile>> room4TileGrid = new ArrayList<>();
    Door room4d1 = new Door();
    Door room4d2 = new Door();
    room4d1.setTileCoord(new Posn(13, 7));
    room4d2.setTileCoord(new Posn(9, 14));

    Tile room4DoorTile1 = new Tile();
    Tile room4DoorTile2 = new Tile();

    room4DoorTile1.setDoor(room4d1);
    room4DoorTile2.setDoor(room4d2);

    List<Door> room4doorPos = new ArrayList<>();
    room4doorPos.add(room4d1);
    room4doorPos.add(room4d2);

    ArrayList<Tile> room4row1 = new ArrayList<>();
    ArrayList<Tile> room4row2 = new ArrayList<>();

    room4row1.add(room4DoorTile1);
    room4row1.add(new Tile(false));
    room4row1.add(new Tile(false));

    room4row2.add(new Tile(false));
    room4row2.add(new Tile(false));
    room4row2.add(room4DoorTile2);

    room4TileGrid.add(room4row1);
    room4TileGrid.add(room4row2);

    room4.setDoorPositions(room4doorPos);
    room4.setUpperLeft(new Posn(13, 7));
    room4.setTileGrid(room4TileGrid);
    room4.setxDim(2);
    room4.setyDim(3);

    rooms.add(room1);
    rooms.add(room2);
    rooms.add(room3);
    rooms.add(room4);

    Hallway hallway1 = new Hallway();
    List<Door> hallway1Doors = new ArrayList<>();
    hallway1Doors.add(room1Door);
    hallway1Doors.add(room2Door1);
    hallway1.setDoors(hallway1Doors);

    List<ArrayList<Tile>> hallway1TileSegments = new ArrayList<>();
    ArrayList<Tile> hallway1Segment = new ArrayList<>();
    hallway1Segment.add(new Tile(false));
    hallway1Segment.add(new Tile(false));
    hallway1Segment.add(new Tile(false));
    hallway1Segment.add(new Tile(false));
    hallway1TileSegments.add(hallway1Segment);

    hallway1.setTileSegments(hallway1TileSegments);

    Hallway hallway2 = new Hallway();
    List<Door> hallway2Doors = new ArrayList<>();
    hallway2Doors.add(room2Door2);
    hallway2Doors.add(room3d1);
    hallway2.setDoors(hallway2Doors);

    List<Posn> hallway2Waypoints = new ArrayList<>();
    Posn hallway2Waypoint = new Posn(6,9);
    hallway2Waypoints.add(hallway2Waypoint);

    List<ArrayList<Tile>> hallway2TileSegments = new ArrayList<>();
    ArrayList<Tile> hallway2Segment1 = new ArrayList<>();
    ArrayList<Tile> hallway2Segment2 = new ArrayList<>();

    hallway2Segment1.add(new Tile(false));
    hallway2Segment1.add(new Tile(false));
    hallway2Segment1.add(new Tile(false));
    hallway2TileSegments.add(hallway2Segment1);

    hallway2Segment2.add(new Tile(false));
    hallway2Segment2.add(new Tile(false));
    hallway2Segment2.add(new Tile(false));
    hallway2Segment2.add(new Tile(false));
    hallway2Segment2.add(new Tile(false));
    hallway2TileSegments.add(hallway2Segment2);

    hallway1.setTileSegments(hallway1TileSegments);
    hallway2.setTileSegments(hallway2TileSegments);
    hallway2.setWaypoints(hallway2Waypoints);

    Hallway hallway3 = new Hallway();
    List<Posn> waypoints3 = new ArrayList<>();
    waypoints3.add(new Posn(13, 3));
    hallway3.setWaypoints(waypoints3);

    List<ArrayList<Tile>> h3Segments = new ArrayList<>();
    ArrayList<Tile> h3Segment1 = new ArrayList<>();
    ArrayList<Tile> h3Segment2 = new ArrayList<>();

    h3Segment1.add(new Tile(false));
    h3Segment1.add(new Tile(false));
    h3Segment1.add(new Tile(false));

    h3Segment2.add(new Tile(false));
    h3Segment2.add(new Tile(false));
    h3Segment2.add(new Tile(false));

    h3Segments.add(h3Segment1);
    h3Segments.add(h3Segment2);

    List<Door> doors3 = new ArrayList<>();
    doors3.add(room3d2);
    doors3.add(room4d1);

    hallway3.setDoors(doors3);
    hallway3.setTileSegments(h3Segments);

    hallways.add(hallway3);

    hallways.add(hallway1);
    hallways.add(hallway2);

    complicatedLevel.setRooms(rooms);
    complicatedLevel.setHallways(hallways);

    return complicatedLevel;
  }
}
