package GameObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents humanoids users can control in our game. Actors should have information about what they
 * can see.
 */
public abstract class Actor {
  private Posn position;
  private String name;

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

  public abstract boolean isPlayer();

}


