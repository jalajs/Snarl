public class ExitKey extends Collectables {
  private boolean isCollected;
  private Posn tileLocation;


  public ExitKey() {
    this.isCollected = false;
    this.tileLocation = new Posn(0, 0);
  }

  public boolean isCollected() {
    return isCollected;
  }

  public void setCollected(boolean collected) {
    isCollected = collected;
  }

  public Posn getTileLocation() {
    return tileLocation;
  }

  public void setTileLocation(Posn tileLocation) {
    this.tileLocation = tileLocation;
  }
}
