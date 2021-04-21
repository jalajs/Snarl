package GameObjects;

import java.nio.charset.StandardCharsets;

import Action.Action;

/**
 * Represents a type of actor in the game. Adversaries are trying to eliminate players from the level.
 */
public class Adversary extends Actor {
  // represents whether the adversary is internal, an AI, or something else entirely
  private AdversaryType type;
  private String name;
  private int damagePoints;

  /**
   * This is a blank slate constructor for testing
   */
  public Adversary() {
  }

  /**
   * This initializes an adversary with knowledge of where it will source its actions
   * @param type
   */
  public Adversary(AdversaryType type, String name) {
    super(name);
    this.type = type;
    if (type.equals(AdversaryType.ZOMBIE)) {
      damagePoints = 25;
    } else {
      damagePoints = 50;
    }
  }

  public int getDamagePoints() {
    return damagePoints;
  }

  public void setDamagePoints(int damagePoints) {
    this.damagePoints = damagePoints;
  }

  /**
   * Renders the visual representation of an adversary.
   * @return the string for the representation of an adversary
   */
  public String representation() {
    return type.equals(AdversaryType.ZOMBIE) ? "Z" : "G";
  }

  /**
   * Return false, as an adversary is not a player
   * @return
   */
  @Override
  public boolean isPlayer() {
    return false;
  }

  public Action turn() {
    return null;
  }

  public AdversaryType getType() {
    return type;
  }

  public void setType(AdversaryType type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }
}
