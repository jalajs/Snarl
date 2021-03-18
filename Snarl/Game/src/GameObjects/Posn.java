package GameObjects;

import java.util.Objects;

/**
 * Represents a cartesian position.
 */
public class Posn {
  private int row;
  private int col;


  public Posn(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Posn posn = (Posn) o;
    return row == posn.row && col == posn.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
