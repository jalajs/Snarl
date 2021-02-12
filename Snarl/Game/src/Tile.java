public class Tile {
  private boolean isOccupied;
  private boolean isWall;


  public Tile() {
    this.isOccupied = false;
    this.isWall = false;
  }

  public Tile(boolean isOccupied, boolean isWall) {
    this.isOccupied = isOccupied;
    this.isWall = isWall;
  }
}
