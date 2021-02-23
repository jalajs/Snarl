/**
 * Represents a type of actor in the game. Players try to find the exit in the level and can be expelled
 * from levels.
 */
public class Player extends Actor {

  public Player() {
  }


  /**
   * Renders the visual representation of a Player
   * @return the string for the representation of a player
   */
  public String representation() {
    return "O";
  }
}
