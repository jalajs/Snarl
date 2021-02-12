public class Door {
  private Posn tileCoord;
  private Hallway hallway;
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

  public boolean isLevelExit() {
    return isLevelExit;
  }

  public void setLevelExit(boolean levelExit) {
    isLevelExit = levelExit;
  }
}
