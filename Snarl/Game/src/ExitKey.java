public class ExitKey implements Collectable {
  private boolean isCollected;

  public ExitKey() {
    this.isCollected = false;
  }

  public boolean isCollected() {
    return isCollected;
  }

  public void setCollected(boolean collected) {
    isCollected = collected;
  }

  @Override
  public String toString(){
    return "K";
  }
}
