import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all tests for level representation
 */
public class LevelRepresentationTest {
  private final TestUtils testUtils = new TestUtils();

  @Test
  /**
   * this test verifies the representation of a variety of rooms
   */
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
  /**
   * this test verifies the representation of an empty level
   */
  public void testEmptyLevel() {
    Level level = new Level();
    level.initGrid();
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
  /**
   * this test adds rooms to a level and verifies the representation is correct
   */
  public void testLevelWithManyRooms() {
    Level complicatedLevel = new Level();
    Room simpleRoom = testUtils.createSimpleRoom();
    ArrayList<Room> rooms = new ArrayList<Room>();
    rooms.add(simpleRoom);
    complicatedLevel.setRooms(rooms);
    complicatedLevel.initGrid();
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
    complicatedLevel.initGrid();
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
  /**
   * adds rooms and hallways to level and verifies representation
   */
  public void testLevelWithHallwaysAndRooms() {
    Room complicatedRoom = testUtils.createComplicatedRoom();
    Room roomWithDoor = testUtils.createSimpleRoomWithDoor();

    ArrayList<Room> roomsWithDoors = new ArrayList<>();
    roomsWithDoors.add(roomWithDoor);
    roomsWithDoors.add(complicatedRoom);

    Level level = new Level();
    level.setRooms(roomsWithDoors);

    level.initGrid();
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

    level.initGrid();
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
  /**
   * tests the representation of a complicated level with given dimensions and many rooms and hallways
   */
  public void testComplicatedLevel() {
    Level complicatedLevel = new Level(15, 10);
    complicatedLevel.initGrid();
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

    complicatedLevel = testUtils.createComplicatedLevel();
    complicatedLevel.initGrid();
    String complicatedLevelString = complicatedLevel.createLevelString();
    System.out.print(complicatedLevelString);
    assertEquals(
            "...    ...\n" +
                    "..X    ...\n" +
                    "..|....|.|\n" +
                    "         .\n" +
                    "         .\n" +
                    "         .\n" +
                    "...|.....+\n" +
                    "XX.X      \n" +
                    "....      \n" +
                    "...|      \n" +
                    "   .      \n" +
                    "   .      \n" +
                    "   .      \n" +
                    "   +...|..\n" +
                    "       ..|", complicatedLevelString);

  }
}
