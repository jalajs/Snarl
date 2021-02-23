/**
 * Represents the key players can connect to unlock the exit.
 */
public class ExitKey implements Collectable {
  private boolean isCollected;
  private Posn location;

  public ExitKey(Posn location) {
    this.isCollected = false;
    this.location = location;
  }

  public boolean isCollected() {
    return isCollected;
  }

  public void setCollected(boolean collected) {
    isCollected = collected;
  }

  public Posn getLocation() { return this.location; }

  public void setLocation(Posn location) { this.location = location; }

  @Override
  public String toString(){
    return "K";
  }
}
