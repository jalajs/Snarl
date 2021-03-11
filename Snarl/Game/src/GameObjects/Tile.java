package GameObjects;

/**
 * Represents a game tile and contains information on if it is a wall and what is on the tile
 */
public class Tile {
  private Actor occupier;
  private Collectable collectable;
  private Door door;
  private boolean isWall;
  private Posn position;

  /**
   * Default tile constructor
   */
  public Tile() {
    this.occupier = null;
    this.collectable = null;
    this.door = null;
    this.isWall = false;
  }

  /**
   * GameObjects.Tile constructor that allows specification if it is a wall or not
   *
   * @param isWall indicating if it is a wall or not
   */
  public Tile(boolean isWall) {
    this.isWall = isWall;
    this.occupier = null;
    this.collectable = null;
    this.door = null;
  }


  @Override
  /**
   * Returns string representation of the tile, dependent on what is on the tile
   */
  public String toString() {
    if (isWall) {
      return "X";
    } else if (occupier != null) {
      return occupier.representation();
    } else if (collectable != null) {
      return collectable.toString();
    } else if (door != null) {
      return door.toString();
    } else {
      return ".";
    }
  }

  /**
   * If the given actor is a player, then the tile is not interactable only if it contains another
   * player or if it contains the exit door and the level is not exitable. If the given actor is an
   * adversary the the tile is always interactable Note: this is called after is Move valid, so we
   * know the tile is traversable and accessible
   *
   * @param actor      the actor trying to do the interaction
   * @param isExitable indicates if the exit door can be interacted with
   * @return whether or not the interaction is valid
   */
  public boolean isInteractable(Actor actor, boolean isExitable) {
    if (actor.isPlayer()) {
      if (this.getDoor() != null) {
        return !door.isLevelExit() || (door.isLevelExit() && isExitable);
      }
      if (this.getOccupier() != null) {
        return !occupier.isPlayer();
      }
    }

    return true;

  }

  /**
   * If a player lands on this tile, this interaction will take place
   * @return the string representation of the interaction type
   *         Possible interaction types are: Key, Adversary, None
   */
  public String getInteraction() {
    if (door != null) {
      if(this.door.isLevelExit()) {
        return "Exit";
      } else {
        return "None";
      }
    }
    if (collectable != null) {
      return "Key";
    }
    if (occupier != null) {
      if (!occupier.isPlayer()) {
        return "Adversary";
      }
    }
      return "None";
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

  public boolean getisWall() {
    return isWall;
  }

  public void setisWall(boolean isWall) {
    this.isWall = isWall;
  }

  public Posn getPosition() {
    return position;
  }

  public void setPosition(Posn position) {
    this.position = position;
  }

}
