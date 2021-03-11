package User;

import java.util.List;

import GameObjects.Action;
import GameObjects.Posn;
import GameObjects.Tile;
import GameState.GameState;

/**
 * This interface represents an actual human playing the game.
 */
public interface User {

  /**
   * Returns the name the user registered with
   * @return the name
   */
  String getName();

  /**
   * Returns the current position of the user within the game
   * @return the position of the user
   */
  Posn getCurrentPosition();

  /**
   * Updates the user with any changes that happened in the game state. This is only called in GameManager
   * @param updatedSurroundings the new surrounding of the user
   * @param isExitable whether or not the door has been unlocked
   */
  void update(List<List<Tile>> updatedSurroundings, boolean isExitable);

  /**
   * Prompts the user for its turn/move. The user does its move and returns an Action ack to the GameManager
   * @return the action object the user does from this move.
   */
  Action turn();



}
