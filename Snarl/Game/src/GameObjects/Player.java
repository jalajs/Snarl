package GameObjects;

/**
 * Represents a type of actor in the game. Players try to find the exit in the level and can be expelled
 * from levels.
 */
public class Player extends Actor {
  private String name;
  /**
   * This is blank slate constructor for testing
   */
  public Player() { }

  /**
   * This initializes a player with the given name. All players in a game
   * will have unique names.
   * @param name
   */
  public Player(String name) {
    super(name);
  }

  /**
   * Renders the visual representation of a GameObjects.Player
   * @return the string for the representation of a player
   */
  public String representation() {
    return "O";
  }

  /**
   * Returns true as this player is a player
   * @return true
   */
  @Override
  public boolean isPlayer() {
    return true;
  }
}
