package Observer;

import GameState.GameState;

public interface Observer {


  /**
   * This method updates all the information needed to render the view to the 3rd party and
   * displays it to the console
   * @param gameState represents the current gameState*/
  void update(GameState gameState);

}
