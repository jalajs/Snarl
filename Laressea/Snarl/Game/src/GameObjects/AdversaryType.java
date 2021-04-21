package GameObjects;

public enum AdversaryType {
  ZOMBIE("zombie"),
  GHOST("ghost");

  public String type;
  AdversaryType(String type) {
    this.type = type;
  }
}
