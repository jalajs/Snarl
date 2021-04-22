package GameObjects;

/**
 * Represents a type of actor in the game. Players try to find the exit in the level and can be expelled
 * from levels.
 */
public class Player extends Actor {
  private String name;
  private int id = 0;
  private int hitPoints = 100;
  /**
   * This is blank slate constructor for testing
   */
  public Player() { }

  public Player(int id) {
    this.id = id;
  }

  /**
   * This initializes a player with the given name. All players in a game
   * will have unique names.
   * @param name
   */
  public Player(String name) {
    super(name);
  }

  public Player(String name, int id, int hitPoints) {
    super(name);
    this.id = id;
    this.hitPoints = hitPoints;
  }


  /**
   * Renders the visual representation of a GameObjects.Player
   * @return the string for the representation of a player
   */
  public String representation() {
    return id == 0 ? "O" : String.valueOf(id);
  }

  /**
   * Returns true as this player is a player
   * @return true
   */
  @Override
  public boolean isPlayer() {
    return true;
  }

  public int getHitPoints() { return this.hitPoints; }

  public void setHitPoints(int hitPoints) { this.hitPoints = hitPoints; }

  public void subtractFromHitPoints(int damage) { this.hitPoints = this.hitPoints - damage; }
}
