import org.junit.Test;
import static org.junit.Assert.*;

import GameObjects.Posn;

public class PosnTest {

  @Test
  public void testPosnEquality() {
    Posn p1 = new Posn(3, 3);
    Posn p2 = new Posn(3, 3);
    Posn p3 = new Posn(1, 2);
    Posn p4 = new Posn(2, 1);

    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
    assertFalse(p1.equals(p3));
    assertFalse(p3.equals(p4));
  }
}
