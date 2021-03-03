package GameObjects;

import java.util.Objects;

/**
 * Represents a cartesian position.
 */
public class Posn {
  private int x;
  private int y;


  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Posn posn = (Posn) o;
    return x == posn.x && y == posn.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
