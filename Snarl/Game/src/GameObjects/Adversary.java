package GameObjects;

/**
 * Represents a type of actor in the game. Adversaries are trying to eliminate players from the level.
 */
public class Adversary extends Actor {
  // represents whether the adversary is internal, an AI, or something else entirely
  private String type;
  private String name;

  /**
   * This is a blank slate constructor for testing
   */
  public Adversary() {
  }

  /**
   * This initializes an adversary with knowledge of where it will source its actions
   * @param type
   */
  public Adversary(String type, String name) {
    this.type = type;
    this.name = name;
  }

  /**
   * Renders the visual representation of an adversary.
   * @return the string for the representation of an adversary
   */
  public String representation() {
    return "#";
  }

  @Override
  public boolean isPlayer() {
    return false;
  }

  public Action turn() {
    return null;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }
}
