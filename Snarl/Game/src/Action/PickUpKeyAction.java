package Action;

import Action.Action;
import GameObjects.Posn;
import GameState.GameState;

public class PickUpKeyAction implements Action {


  /**
   * Picks up the key and unlocks the exit in the level
   * @param gameState is the gamestate in which the action is to be performed
   */
  @Override
  public void execute(GameState gameState) {
    gameState.handleKeyCollection();
  }

  @Override
  public String getType() {
    return "Pick Up Key";
  }


}
