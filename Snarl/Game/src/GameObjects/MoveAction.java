package GameObjects;

import GameState.GameState;

/**
 * This action represents a Move
 */
public class MoveAction implements Action {
  private Posn destination;
  private Posn currentPosition;

  /**
   * This constructor builds a move with a destination and a currentPosition
   */
  public MoveAction(Posn destination, Posn currentPosition) {
    this.destination = destination;
    this.currentPosition = currentPosition;
  }

  @Override
  public void execute(GameState gameState) {

  }

  @Override
  public String getType() {
    return "move";
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
}
