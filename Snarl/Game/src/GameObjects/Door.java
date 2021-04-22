package GameObjects;

/**
 * Represents a door within our level structure. Doors allow rooms to be connected via hallways, and
 * could also lead to the exit of the level for Players.
 */
public class Door {
  private Posn tileCoord;
  private Hallway hallway;
  private Room room;
  private boolean isLevelExit;


  public Door() {
    this.tileCoord = new Posn(0, 0);
    this.hallway = null;
    this.isLevelExit = false;
  }

  public Posn getTileCoord() {
    return tileCoord;
  }

  public void setTileCoord(Posn tileCoord) {
    this.tileCoord = tileCoord;
  }

  public Hallway getHallway() {
    return hallway;
  }

  public void setHallway(Hallway hallway) {
    this.hallway = hallway;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public boolean isLevelExit() {
    return isLevelExit;
  }

  public void setLevelExit(boolean levelExit) {
    isLevelExit = levelExit;
  }

  /**
   * Outputs how a door would be rendered as a String visually
   * @return a door as a string
   */
  @Override
  public String toString() {
    return isLevelExit ? "E" : "|";
  }
}
