package Action;

import GameObjects.Posn;
import GameState.GameState;

/**
 * This Interface represents an Action taken by an actor
 */
public interface Action {

  /**
   * This method executes the action on the given GameState
   * @param gameState is the gamestate in which the action is to be performed
   *
   */
  void execute(GameState gameState);

  String getType();

  /**
   * This method returns the actions destination
   * @return
   */
  Posn getDestination();

  /**
   * Sets the destination position for this action
   */
  void setDestination(Posn destination);

  InteractionType getInteractionType();

  void setInteractionType(InteractionType interactionType);

  /** Gets the current position/ "from" position for this aciton
   *
   *
   * @return
   */
  Posn getCurrentPosition();

  void setDamage(int damage);

  String getVictimName();

  void setVictimName(String victimName);
}
