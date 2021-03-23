package Action;

import Action.Action;
import GameObjects.Posn;
import GameState.GameState;

public class EjectAction implements Action {
  private String ejectedPlayerName;

  /**
   * This constructor creates an EjectAction with the name of the player to be removed upon exectution
   * @param ejectedPlayerName
   */
  public EjectAction(String ejectedPlayerName) {
    this.ejectedPlayerName = ejectedPlayerName;
  }

  // ToDo: implement removing player from the game
  @Override
  public void execute(GameState gameState) {

  }

  @Override
  public String getType() {
    return "Eject";
  }

}
