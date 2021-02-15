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
    List<Hallway> hallways = new ArrayList<>();
    hallways.add(hallway);

    System.out.println();
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
}
