public class Tile {
  private Humanoid occupier;
  private Collectable collectable;
  private Door door;
  private boolean isWall;

  public Humanoid getOccupier() {
    return occupier;
  }

  public void setOccupier(Humanoid occupier) {
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

  public Tile() {
    this.occupier = null;
    this.collectable = null;
    this.door = null;
    this.isWall = false;
  }

  public Tile(boolean isWall) {
   this.isWall = isWall;
    this.occupier = null;
    this.collectable = null;
    this.door = null;
  }


  @Override
  public String toString() {
    if (isWall) {
      return "X";
    }
    else if (occupier != null) {
      return occupier.toString();
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
}
