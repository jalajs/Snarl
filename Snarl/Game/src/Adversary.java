/**
 * Represents a type of actor in the game. Adversaries are trying to eliminate players from the level.
 */
public class Adversary extends Actor {


  public Adversary() {

  }

  /**
   * Renders the visual representation of an adversary.
   * @return the string for the representation of an adversary
   */
  public String representation() {
    return "#";
  }
}
