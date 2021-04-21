package GameObjects;

/**
 * Represents humanoids users can control in our game. Actors should have information about what they
 * can see.
 */
public abstract class Actor {
  private Posn position;
  public String name;

  public Actor() {};

  public Actor(String name) {
    this.name = name;
  }

  public Posn getPosition() {
    return position;
  }

  public void setPosition(Posn position) {
    this.position = position;
  }

  public void setName(String name) { this.name = name; }

  public String getName() { return this.name; }
  /**
   * Returns the visual representation of the GameObjects.Actor.
   * @return a string for how an actor is represented.
   */
  public abstract String representation();

  /**
   * Return true if the implementing class is a player (and false if not)
   * @return
   */
  public abstract boolean isPlayer();

}


