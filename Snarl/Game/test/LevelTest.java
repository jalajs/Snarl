import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class LevelTest {


  @Test
  public void testRoomToString() {
    // draw a simple un occuppied un walled room
    Room simpleRoom = new Room();
    List<ArrayList<Tile>> simpleTileGrid = new ArrayList<>();
    ArrayList<Tile> simpleTileRow = new ArrayList<>();
    simpleTileRow.add(new Tile(false));
    simpleTileRow.add(new Tile(false));

    simpleTileGrid.add(simpleTileRow);
    simpleTileGrid.add(simpleTileRow);

    simpleRoom.setTileGrid(simpleTileGrid);

    assertEquals(simpleRoom.toString(), "..\n..");

    // draw a room with walls
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

    assertEquals(walledRoom.toString(), "XXX\nXXX\nXXX");

    // draw a complicated room with doors and humanoids and collectables
    Room complicatedRoom = new Room();
    ExitKey key = new ExitKey();
//    Actor actor = new Actor();
    Door door = new Door();
    Tile keyTile =  new Tile(false);
    Tile humanoidTile = new Tile(false);
    Tile doorTile = new Tile(false);
 //   humanoidTile.setOccupier(actor);
    keyTile.setCollectable(key);
    doorTile.setDoor(door);

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
    complicatedTileRow2.add(humanoidTile);

    complicatedTileRow3.add(doorTile);
    complicatedTileRow3.add(humanoidTile);
    complicatedTileRow3.add(new Tile(false));

    complicatedTileRow4.add(humanoidTile);
    complicatedTileRow4.add(new Tile(false));
    complicatedTileRow4.add(new Tile(true));

    complicatedTileGrid.add(complicatedTileRow1);
    complicatedTileGrid.add(complicatedTileRow2);
    complicatedTileGrid.add(complicatedTileRow3);
    complicatedTileGrid.add(complicatedTileRow4);


    complicatedRoom.setTileGrid(complicatedTileGrid);

    assertEquals(complicatedRoom.toString(), "K..\n" +
            "XX*\n" +
            "|*.\n" +
            "*.X");


  }
@Test
  public void testLevel() {
    Level level1 = new Level();
    assertEquals("          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          ", level1.createLevelString());
    Level level2 = new Level();
  Room simpleRoom = new Room();
  List<ArrayList<Tile>> simpleTileGrid = new ArrayList<>();
  ArrayList<Tile> simpleTileRow = new ArrayList<>();
  simpleTileRow.add(new Tile(false));
  simpleTileRow.add(new Tile(false));

  simpleTileGrid.add(simpleTileRow);
  simpleTileGrid.add(simpleTileRow);

  simpleRoom.setTileGrid(simpleTileGrid);
  simpleRoom.setUpperLeft(new Posn(0, 0));
  simpleRoom.setxDim(2);
  simpleRoom.setyDim(2);

  ArrayList<Room> rooms1 = new ArrayList<Room>();
  rooms1.add(simpleRoom);
  level2.setRooms(rooms1);
  assertEquals("..        \n" +
          "..        \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "          ", level2.createLevelString());


  // draw a complicated room with doors and humanoids and collectables
  Room complicatedRoom = new Room();
  ExitKey key = new ExitKey();
//    Actor actor = new Actor();
  Door door = new Door();
  Tile keyTile =  new Tile(false);
  Tile humanoidTile = new Tile(false);
  Tile doorTile = new Tile(false);
  //   humanoidTile.setOccupier(actor);
  keyTile.setCollectable(key);
  doorTile.setDoor(door);

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
  complicatedTileRow2.add(humanoidTile);

  complicatedTileRow3.add(doorTile);
  complicatedTileRow3.add(humanoidTile);
  complicatedTileRow3.add(new Tile(false));

  complicatedTileRow4.add(humanoidTile);
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

  rooms1.add(complicatedRoom);
  level2.setRooms(rooms1);
  assertEquals("..        \n" +
          "..        \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "      K.. \n" +
          "      XX. \n" +
          "      |.. \n" +
          "      ..X \n" +
          "          ", level2.createLevelString());


  Level level3 = new Level();
  ArrayList<Room> roomsWithDoors = new ArrayList<>();
  Room roomWithDoor = new Room();
  roomWithDoor.setUpperLeft(new Posn(0, 0));

  List<ArrayList<Tile>> simpleRoomWithDoor = new ArrayList<>();
  ArrayList<Tile> bottomRow = new ArrayList<>();
  Tile doorTile2 = new Tile(false);
  Door door2 = new Door();
  doorTile2.setDoor(door2);
  bottomRow.add(new Tile(false));
  bottomRow.add(doorTile2);

  simpleRoomWithDoor.add(simpleTileRow);
  simpleRoomWithDoor.add(bottomRow);

  roomWithDoor.setTileGrid(simpleRoomWithDoor);

  List<Door> doorPos = new ArrayList<>();

  door2.setTileCoord(new Posn(1, 1));
  roomWithDoor.setDoorPositions(doorPos);
  roomWithDoor.setyDim(2);
  roomWithDoor.setyDim(2);


  roomsWithDoors.add(roomWithDoor);
  roomsWithDoors.add(complicatedRoom);
  level3.setRooms(roomsWithDoors);
  System.out.print(roomsWithDoors.size());

  assertEquals("..        \n" +
          "..        \n" +
          "          \n" +
          "          \n" +
          "          \n" +
          "      K.. \n" +
          "      XX. \n" +
          "      |.. \n" +
          "      ..X \n" +
          "          ", level3.createLevelString());

  }

}
