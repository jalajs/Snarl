import org.junit.Test;

import GameObjects.Level;
import GameObjects.Hallway;
import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Room;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HallwayTest {
  private final TestUtils testUtils = new TestUtils();

  /**
   * the purpose of this test to see that connectDoorsToRooms sets the room field of each of the
   * hallways doors
   */
  @Test
  public void testConnectDoorsToRooms() {
    // this level looks like
    // ROOM1----hallway----ROOM2
    Level level = testUtils.createLevelForTestingDoorConnection();
    Hallway hallway = level.getHallways().get(0);
    List<Room> rooms = level.getRooms();

    // first check if both hallway door's rooms a re null
    for (Door door : hallway.getDoors()) {
      assertNull(door.getRoom());
    }
    hallway.connectDoorsToRooms(rooms);
    // verify the door's rooms are no longer null
    for (Door door : hallway.getDoors()) {
      assertNotNull(door.getRoom());
    }
  }
}
