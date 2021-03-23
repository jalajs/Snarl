package Observer;

import GameState.GameState;

public class LocalObserver implements Observer {

  /**
   *  This method updates all the information needed to render the view to the 3rd party and
   *  displays it to the console
   * @param gameState represents the current gameState
   */
  public void update(GameState gameState) {
    String exitStatus = gameState.isExitable() ? "open" : "closed";
    System.out.println("The level exit is " + exitStatus);
    System.out.println(gameState.getLevel().createLevelString());
  }


}
