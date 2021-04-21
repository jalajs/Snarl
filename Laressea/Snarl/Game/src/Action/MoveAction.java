package Action;

import GameObjects.Posn;
import GameState.GameState;

/**
 * This action represents a Move
 */
public class MoveAction implements Action {
  private Posn destination;
  private Posn currentPosition;
  private InteractionType interactionType = InteractionType.NONE;
  private int damage;
  private String victimName;

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
    this.damage = 0;
    this.victimName = "";
  }

  /**
   * This constructor builds a move with a destination and a currentPosition, as well as a damage number
   */
  public MoveAction(Posn destination, Posn currentPosition, int damage) {
    if (destination == null) {
      this.destination = currentPosition;
    } else {
      this.destination = destination;
    }
    this.currentPosition = currentPosition;
    this.damage = damage;
    this.victimName = "";
  }

  public MoveAction() {
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

  public InteractionType getInteractionType() {
    return interactionType;
  }

  public void setInteractionType(InteractionType interactionType) {
    this.interactionType = interactionType;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  @Override
  public String getVictimName() {
    return victimName;
  }

  @Override
  public void setVictimName(String victimName) {
    this.victimName = victimName;
  }
}
