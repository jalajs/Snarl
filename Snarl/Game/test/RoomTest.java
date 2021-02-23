import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {
  private final TestUtils testUtils = new TestUtils();

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
}
