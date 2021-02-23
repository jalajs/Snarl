import java.util.ArrayList;
import java.util.List;

/**
 * Represents humanoids users can control in our game. Actors should have information about what they
 * can see.
 */
public abstract class Actor {
  private Posn position;

  public Posn getPosition() {
    return position;
  }

  public void setPosition(Posn position) {
    this.position = position;
  }
  /**
   * Returns the visual representation of the Actor.
   * @return a string for how an actor is represented.
   */
  public abstract String representation();

}


