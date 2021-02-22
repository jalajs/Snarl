/**
 * Represents the game's current state and its rules.
 */
public interface GameState {

  /**
   *   Level object is created by random level generator in GameManager
   *   Also resets all the other fields according to the generated Level
   * @param newLevel
   */
  void setLevel(Level newLevel);

  /**
   *   Method determines if a player can move to the given location
   *   A move is valid if the tile is unoccupied/un-walled and near enough
   * @param player
   * @param destination
   * @return
   */
  boolean validMove(Player player, Posn destination);

  /**
   *   Checks if all players are still in the game and that none have exited
   *   Calls endGame()
   * @return
   */
  boolean isGameComplete();


  /**
   *  Moves a specific player to desired location
   *   Calls validMove() before running the logics of the move, then calls updateGame()
   * @param player
   * @param destination
   */
  void movePlayer(Player player, Posn destination);

  /**
   *   Updates and rerenders the level with the newest data
   * @return
   */
  void updateGame();

  /**
   *  Returns to the gameManager that game is done and the conditions that resulted in game finishing (loss/win)
   */
  void endGame();


}
