import org.junit.Test;

import GameObjects.Level;
import GameObjects.Hallway;
import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Room;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

  /**
   *
   */
  @Test
  public void testIsPosnInSegment() {
    Hallway hallway = new Hallway();
    assertTrue(hallway.isPosnInSegment(new Posn(0, 0), new Posn(0,10), new Posn(0, 3)));
    assertTrue(hallway.isPosnInSegment(new Posn(5, 5), new Posn(10,5), new Posn(6, 5)));
    assertTrue(hallway.isPosnInSegment(new Posn(5, 5), new Posn(10,5), new Posn(10, 5)));
    assertFalse(hallway.isPosnInSegment(new Posn(0, 1), new Posn(0,20), new Posn(2,2)));
    assertTrue(hallway.isPosnInSegment(new Posn(0,3), new Posn(6,3), new Posn(1,3)));
  }

  @Test
  public void testIsPointInRange() {
    Hallway hallway = new Hallway();
    Posn point = new Posn(1, 3);
    Posn waypoint = new Posn(0, 3);
    Posn door2 = new Posn(6, 3);

    assertTrue(hallway.isPointInRange(point.getRow(), waypoint.getRow(), door2.getRow()));
  }


  @Test
  public void testIsPosnInHallway() {
    Hallway hallway = new Hallway();
    List<Posn> waypoints = new ArrayList<>();
    List<Door> doors = new ArrayList<>();
    Door door1 = new Door();
    door1.setTileCoord(new Posn(0,0));
    Door door2 = new Door();
    door2.setTileCoord(new Posn(6,3));
    waypoints.add(new Posn(0,3));
    doors.add(door1);
    doors.add(door2);

    //   |--+
    //      |
    //      |
    //      ..
    //      - (6, 3)

    hallway.setWaypoints(waypoints);
    hallway.setDoors(doors);

   assertTrue(hallway.isPosnInHallway(new Posn(0,0)));
   assertTrue(hallway.isPosnInHallway(new Posn(6,3)));
   assertTrue(hallway.isPosnInHallway(new Posn(0,3)));
   assertTrue(hallway.isPosnInHallway(new Posn(1,3))); // SHOULD PASS
   assertTrue(hallway.isPosnInHallway(new Posn(0,1)));
  }
}
