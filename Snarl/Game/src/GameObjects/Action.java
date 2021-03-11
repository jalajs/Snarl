package GameObjects;

import GameState.GameState;

/**
 * This Interface
 */
public interface Action {

  /**
   * This method executes the action on the given GameState
   * @param gameState is the gamestate in which the action is to be performed
   *
   */
  void execute(GameState gameState);


  /**
   * This method returns the type of action. It is unique for every implementing class.
   *
   */
  String getType();
}
