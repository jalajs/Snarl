package Action;

import Action.Action;
import GameObjects.Posn;
import GameState.GameState;

public class DoNothingAction implements Action {

  /**
   * At the moment, this does nothing. It's possible that in the future doing nothing could
   * have reprecussions we would implement here.
   *
   * @param gameState is the gamestate in which the action is to be performed
   */
  @Override
  public void execute(GameState gameState) {
  }

  @Override
  public String getType() {
    return "Do Nothing";
  }


}
