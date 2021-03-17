import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.Door;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Room;
import GameObjects.Tile;

/**
 * Contains utils methods for creating data so that the tests are more readable
 */
public class TestUtils {

  /**
   * Create a simple 2x2 room with no walls
   */
  public Room createSimpleRoom() {
    Room simpleRoom = new Room();
    Tile[][] simpleTileGrid = new Tile[2][2];
    Tile[] simpleTileRow = new Tile[2];
    simpleTileRow[0] = new Tile(false);
    simpleTileRow[1] = new Tile(false);
    simpleTileGrid[0] = simpleTileRow;
    simpleTileGrid[1] = simpleTileRow;
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
    Tile[][] walledTileGrid = new Tile[3][3];
    Tile[] wallTileRow = new Tile[3];
    wallTileRow[0] = new Tile(true);
    wallTileRow[1] = new Tile(true);
    wallTileRow[2] = new Tile(true);
    walledTileGrid[0] = wallTileRow;
    walledTileGrid[1] = wallTileRow;
    walledTileGrid[2] = wallTileRow;
    walledRoom.setTileGrid(walledTileGrid);
    return walledRoom;
  }

  /**
   * Create a simple room with a door
   */
  public Room createSimpleRoomWithDoor() {
    Room roomWithDoor = new Room();
    roomWithDoor.setUpperLeft(new Posn(0, 0));
    Tile[][] simpleRoomWithDoor = new Tile[2][2];
    Tile[] bottomRow = new Tile[2];
    Tile doorTile = new Tile(false);
    Door door = new Door();
    doorTile.setDoor(door);
    bottomRow[0] = new Tile(false);
    bottomRow[1] = doorTile;

    Tile[] simpleTileRow = new Tile[2];
    simpleTileRow[0] = new Tile(false);
    simpleTileRow[1] = new Tile(false);

    simpleRoomWithDoor[0] = simpleTileRow;
    simpleRoomWithDoor[1] = bottomRow;

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
    ExitKey key = new ExitKey(new Posn(-1, -1));
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
    Tile[][] complicatedTileGrid = new Tile[4][3];
    Tile[] complicatedTileRow1 = new Tile[3];
    Tile[] complicatedTileRow2 = new Tile[3];
    Tile[] complicatedTileRow3 = new Tile[3];
    Tile[] complicatedTileRow4 = new Tile[3];
    complicatedTileRow1[0] = keyTile;
    complicatedTileRow1[1] = new Tile(false);
    complicatedTileRow1[2] = new Tile(false);
    complicatedTileRow2[0] = new Tile(true);
    complicatedTileRow2[1] = new Tile(true);
    complicatedTileRow2[2] = playerTile;
    complicatedTileRow3[0] = doorTile;
    complicatedTileRow3[1] = playerTile;
    complicatedTileRow3[2] = new Tile(false);
    complicatedTileRow4[0] = playerTile;
    complicatedTileRow4[1] = new Tile(false);
    complicatedTileRow4[2] = new Tile(true);
    complicatedTileGrid[0] = complicatedTileRow1;
    complicatedTileGrid[1] = complicatedTileRow2;
    complicatedTileGrid[2] = complicatedTileRow3;
    complicatedTileGrid[3] = complicatedTileRow4;
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
    Tile[][] room1TileGrid = new Tile[3][3];
    Tile[] room1Row1 = new Tile[3];
    Tile[] room1Row2 = new Tile[3];
    Tile[] room1Row3 = new Tile[3];

    Door room1Door = new Door();
    Tile room1DoorTile = new Tile();
    room1Door.setTileCoord(new Posn(2,2));
    room1DoorTile.setDoor(room1Door);

    List<Door> room1DoorPos = new ArrayList<>();
    room1DoorPos.add(room1Door);

    room1Row1[0] = new Tile(false);
    room1Row1[1] = new Tile(false);
    room1Row1[2] = new Tile(false);

    room1Row2[0] = new Tile(false);
    room1Row2[1] = new Tile(false);
    room1Row2[2] = new Tile(true);

    room1Row3[0] = new Tile(false);
    room1Row3[1] = new Tile(false);
    room1Row3[2] = room1DoorTile;

    room1TileGrid[0] = room1Row1;
    room1TileGrid[1] = room1Row2;
    room1TileGrid[2] = room1Row3;

    room1.setDoorPositions(room1DoorPos);
    room1.setTileGrid(room1TileGrid);
    room1.setUpperLeft(new Posn(0,0));
    room1.setxDim(3);
    room1.setyDim(3);

    Room room2 = new Room();
    Tile[][] room2TileGrid = new Tile[3][3];
    Tile[] room2Row1 = new Tile[3];
    Tile[] room2Row2 = new Tile[3];
    Tile[] room2Row3 = new Tile[3];
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

    room2Row1[0] = new Tile(false);
    room2Row1[1] = new Tile(false);
    room2Row1[2] = new Tile(false);

    room2Row2[0] = new Tile(false);
    room2Row2[1] = new Tile(false);
    room2Row2[2] = new Tile(false);

    room2Row3[0] = room2DoorTile1;
    room2Row3[1] = new Tile(false);
    room2Row3[2] = room2DoorTile2;

    room2TileGrid[0] = room2Row1;
    room2TileGrid[1] = room2Row2;
    room2TileGrid[2] = room2Row3;

    room2.setDoorPositions(room2DoorPos);
    room2.setTileGrid(room2TileGrid);
    room2.setUpperLeft(new Posn(0,7));
    room2.setxDim(3);
    room2.setyDim(3);

    Room room3 = new Room();

    Tile[][] room3TileGrid = new Tile[4][4];
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

    Tile[] room3row1 = new Tile[4];
    Tile[] room3row2 = new Tile[4];
    Tile[] room3row3 = new Tile[4];
    Tile[] room3row4 = new Tile[4];

    room3row1[0] = new Tile(false);
    room3row1[1] = new Tile(false);
    room3row1[2] = new Tile(false);
    room3row1[3] = room3DoorTile1;


    room3row2[0] = new Tile(true);
    room3row2[1] = new Tile(true);
    room3row2[2] = new Tile(false);
    room3row2[3] = new Tile(true);

    room3row3[0] = new Tile(false);
    room3row3[1] = new Tile(false);
    room3row3[2] = new Tile(false);
    room3row3[3] = new Tile(false);

    room3row4[0] = new Tile(false);
    room3row4[1] = new Tile(false);
    room3row4[2] = new Tile(false);
    room3row4[3] = room3DoorTile2;


    room3TileGrid[0] = room3row1;
    room3TileGrid[1] = room3row2;
    room3TileGrid[2] = room3row3;
    room3TileGrid[3] = room3row4;

    room3.setDoorPositions(room3doorPos);
    room3.setUpperLeft(new Posn(6, 0));
    room3.setTileGrid(room3TileGrid);
    room3.setxDim(4);
    room3.setyDim(4);

    Room room4 = new Room();

    Tile[][] room4TileGrid = new Tile[2][3];
    Tile[] room4row1 = new Tile[3];
    Tile[] room4row2 = new Tile[3];

    Door room4d1 = new Door();
    Door room4d2 = new Door();
    room4d2.setLevelExit(true);
    room4d1.setTileCoord(new Posn(13, 7));
    room4d2.setTileCoord(new Posn(9, 14));

    Tile room4DoorTile1 = new Tile();
    Tile room4DoorTile2 = new Tile();

    room4DoorTile1.setDoor(room4d1);
    room4DoorTile2.setDoor(room4d2);

    List<Door> room4doorPos = new ArrayList<>();
    room4doorPos.add(room4d1);
    room4doorPos.add(room4d2);

    room4row1[0] = room4DoorTile1;
    room4row1[1] = (new Tile(false));
    room4row1[2] = (new Tile(false));

    room4row2[0] = (new Tile(false));
    room4row2[1] = (new Tile(false));
    room4row2[2] =  room4DoorTile2;

    room4TileGrid[0] = room4row1;
    room4TileGrid[1] = room4row2;

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

  /**
   *  creates a level that looks like
   *  ROOM -------------------- ROOM
   *
   * @return returns that level
   */
  public Level createLevelForTestingDoorConnection() {
    List<Room> rooms = new ArrayList<>();
    List<Hallway> hallways = new ArrayList<>();

    Hallway hallway = new Hallway();
    Room room1 = new Room();
    room1.setUpperLeft(new Posn(0,0));
    Room room2 = new Room();
    room2.setUpperLeft(new Posn(0,0));

    Door hallwayDoor1 = new Door();
    Door hallwayDoor2 = new Door();
    Door room1Door = new Door();
    Door room2Door = new Door();

    hallwayDoor1.setTileCoord(new Posn(0, 2));
    hallwayDoor2.setTileCoord(new Posn(0, 7));
    room1Door.setTileCoord(new Posn(0, 2));
    room2Door.setTileCoord(new Posn(0, 7));

    List<Door> hallwayDoors = new ArrayList<>();
    hallwayDoors.add(hallwayDoor1);
    hallwayDoors.add(hallwayDoor2);

    List<Door> room1Doors = new ArrayList<>();
    room1Doors.add(room1Door);
    List<Door> room2Doors = new ArrayList<>();
    room2Doors.add(room2Door);

    hallway.setDoors(hallwayDoors);
    room1.setDoorPositions(room1Doors);
    room2.setDoorPositions(room2Doors);
    rooms.add(room1);
    rooms.add(room2);
    hallways.add(hallway);

    Level level = new Level();
    level.setHallways(hallways);
    level.setRooms(rooms);

    return level;
  }

}
