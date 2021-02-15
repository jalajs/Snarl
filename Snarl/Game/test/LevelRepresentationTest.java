import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class LevelRepresentationTest {
  private TestUtils testUtils = new TestUtils();

  @Test
  // this test verifies the representation of a variety of rooms
  public void testRoomToString() {
    Room simpleRoom = testUtils.createSimpleRoom();
    assertEquals(simpleRoom.toString(), "..\n..");

    Room walledRoom = testUtils.createWalledRoom();
    assertEquals(walledRoom.toString(), "XXX\nXXX\nXXX");

    Room complicatedRoom = testUtils.createComplicatedRoom();
    assertEquals(complicatedRoom.toString(), "K..\n" +
            "XXO\n" +
            "|O.\n" +
            "O.X");
  }

  @Test
  // this test verifies the representation of an empty level
  public void testEmptyLevel() {
    Level level = new Level();
    assertEquals(
            "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          ", level.createLevelString());

  }

  @Test
  // this test adds rooms to a level and verifies the representation is correct
  public void testLevelWithManyRooms() {
    Level complicatedLevel = new Level();
    Room simpleRoom = testUtils.createSimpleRoom();
    ArrayList<Room> rooms = new ArrayList<Room>();
    rooms.add(simpleRoom);
    complicatedLevel.setRooms(rooms);
    assertEquals(
            "..        \n" +
                    "..        \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          ", complicatedLevel.createLevelString());

    Room complicatedRoom = testUtils.createComplicatedRoom();
    rooms.add(complicatedRoom);
    complicatedLevel.setRooms(rooms);
    assertEquals("..        \n" +
            "..        \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "      K.. \n" +
            "      XXO \n" +
            "      |O. \n" +
            "      O.X \n" +
            "          ", complicatedLevel.createLevelString());
  }

  @Test
  // adds rooms and hallways to level and verifies representation
  public void testLevelWithHallwaysAndRooms() {
    Room complicatedRoom = testUtils.createComplicatedRoom();
    Room roomWithDoor = testUtils.createSimpleRoomWithDoor();

    ArrayList<Room> roomsWithDoors = new ArrayList<>();
    roomsWithDoors.add(roomWithDoor);
    roomsWithDoors.add(complicatedRoom);

    Level level = new Level();
    level.setRooms(roomsWithDoors);

    assertEquals("..        \n" +
            ".|        \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "      K.. \n" +
            "      XXO \n" +
            "      |O. \n" +
            "      O.X \n" +
            "          ", level.createLevelString());

    Hallway hallway = testUtils.createHallway();
    List<Hallway> hallways = new ArrayList<>();
    hallways.add(hallway);
    level.setHallways(hallways);

    assertEquals("..        \n" +
            ".|.+      \n" +
            "   .      \n" +
            "   .      \n" +
            "   .      \n" +
            "   .  K.. \n" +
            "   .  XXO \n" +
            "   +..|O. \n" +
            "      O.X \n" +
            "          ", level.createLevelString());
  }

  @Test
  public void testComplicatedLevel() {
    Level complicatedLevel = new Level(15, 10);
    assertEquals(
            "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          \n" +
                    "          ", complicatedLevel.createLevelString());

    List<Room> rooms = new ArrayList<>();
    List<Hallway> hallways = new ArrayList<>();

    Room room1 = new Room();
    List<ArrayList<Tile>> room1TileGrid = new ArrayList<>();
    ArrayList<Tile> room1Row1 = new ArrayList<>();
    ArrayList<Tile> room1Row2 = new ArrayList<>();
    ArrayList<Tile> room1Row3 = new ArrayList<>();
    Door room1Door = new Door();
    Tile room1DoorTile = new Tile();
    room1Door.setTileCoord(new Posn(3,3));
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

    room2Door1.setTileCoord(new Posn(7,3));
    room2Door2.setTileCoord(new Posn(9,3));

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
    room3d1.setTileCoord(new Posn(3, 6));
    room3d2.setTileCoord(new Posn(3, 9));

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
    room4d1.setTileCoord(new Posn(7, 13));
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
    room4.setxDim(3);
    room4.setyDim(3);
    

    rooms.add(room1);
    rooms.add(room2);
    rooms.add(room3);
    rooms.add(room4);

    complicatedLevel.setRooms(rooms);

    assertEquals(
            "...    ...\n" +
                    "..X    ...\n" +
                    "..|....|.|\n" +
                    "         .\n" +
                    "         .\n" +
                    "         .\n" +
                    "...|.....+\n" +
                    "XXX.       \n" +
                    "....      \n" +
                    "...|      \n" +
                    "   .      \n" +
                    "   .      \n" +
                    "   .      \n" +
                    "   +...|..\n" +
                    "       ..|", complicatedLevel.createLevelString());


  }


}
