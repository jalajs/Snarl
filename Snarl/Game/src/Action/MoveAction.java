package Action;

import Action.Action;
import GameObjects.Posn;
import GameState.GameState;

/**
 * This action represents a Move
 */
public class MoveAction implements Action {
  private Posn destination;
  private Posn currentPosition;
  private String interactionType;

  /**
   * This constructor builds a move with a destination and a currentPosition
   */
  public MoveAction(Posn destination, Posn currentPosition) {
    if (destination == null) {
      this.destination = currentPosition;
    } else {
      this.destination = destination;
    }
    this.currentPosition = currentPosition;
    this.interactionType = "";
  }

  @Override
  public void execute(GameState gameState) {

  }

  @Override
  public String getType() {
    return "Move";
  }

  public Posn getDestination() {
    return destination;
  }

  public void setDestination(Posn destination) {
    this.destination = destination;
  }

  public Posn getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(Posn currentPosition) {
    this.currentPosition = currentPosition;
  }

  public void setInteractionType(String interactionType) { this.interactionType = interactionType; }

  public String getInteractionType() { return this.interactionType; }
}
