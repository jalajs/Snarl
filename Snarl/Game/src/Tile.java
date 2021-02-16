/*
 * Represents a game tile and contains information on if it is a wall and what is on the tile
 */
public class Tile {
  private Actor occupier;
  private Collectable collectable;
  private Door door;
  private boolean isWall;

  /*
   * Default tile constructor
   */
  public Tile() {
    this.occupier = null;
    this.collectable = null;
    this.door = null;
    this.isWall = false;
  }

  /*
   * Tile constructor that allows specification if it is a wall or not
   * @param boolean indicating if it is a wall or not
   */
  public Tile(boolean isWall) {
   this.isWall = isWall;
    this.occupier = null;
    this.collectable = null;
    this.door = null;
  }


  @Override
  /*
   * Returns string representation of the tile, dependent on what is on the tile
   */
  public String toString() {
    if (isWall) {
      return "X";
    }
    else if (occupier != null) {
      return occupier.representation();
    }
    else if (collectable != null) {
      return collectable.toString();
    }
    else if (door != null) {
      return door.toString();
    }
    else {
      return ".";
    }
  }

  public Actor getOccupier() {
    return occupier;
  }

  public void setOccupier(Actor occupier) {
    this.occupier = occupier;
  }

  public Collectable getCollectable() {
    return collectable;
  }

  public Door getDoor() {
    return door;
  }

  public void setDoor(Door door) {
    this.door = door;
  }

  public void setCollectable(Collectable collectable) {
    this.collectable = collectable;

  }
}
