import java.util.ArrayList;
import java.util.List;

public class TestUtils {

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
}
