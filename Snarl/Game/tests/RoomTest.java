import org.junit.Test;

import java.util.List;

import GameObjects.Door;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Posn;
import GameObjects.Room;

import static org.junit.Assert.*;

public class RoomTest {
  private final TestUtils testUtils = new TestUtils();

  /**
   * the purpose of this method is to see if isPosninRoom returns true if a position in the room
   */
  @Test
  public void testIsPosninRoom() {
    Room simpleRoom = testUtils.createSimpleRoom();

    assertTrue(simpleRoom.isPosnInRoom(new Posn(0, 0)));
    assertTrue(simpleRoom.isPosnInRoom(new Posn(1, 1)));
    assertFalse(simpleRoom.isPosnInRoom(new Posn(2, 2)));
    assertFalse(simpleRoom.isPosnInRoom(new Posn(-1, 1)));

    Room complicatedRoom = testUtils.createComplicatedRoom();

    assertTrue(complicatedRoom.isPosnInRoom(new Posn(5, 6)));
    assertTrue(complicatedRoom.isPosnInRoom(new Posn(6, 6)));
    assertFalse(complicatedRoom.isPosnInRoom(new Posn(2, 2)));
    assertFalse(complicatedRoom.isPosnInRoom(new Posn(-1, 1)));
  }

  /**
   * the purpose of this test to see that connectDoorsToHallways sets the hallway field of each of the
   * rooms' doors
   */
  @Test
  public void testConnectDoorsToHallways() {
    // this level looks like
    // ROOM1----hallway----ROOM2
    Level level = testUtils.createLevelForTestingDoorConnection();
    List<Hallway> hallways = level.getHallways();
    List<Room> rooms = level.getRooms();

    // first check if the rooms door's hallways are null
    for(Room room : rooms) {
      for (Door door : room.getDoorPositions()) {
        assertNull(door.getHallway());
      }
    }
    for (Room room : rooms) {
      room.connectDoorsToHallways(hallways);
      // verify the door's hallways are no longer null
      for (Door door : room.getDoorPositions()) {
        assertNotNull(door.getHallway());
      }
    }
  }
}
