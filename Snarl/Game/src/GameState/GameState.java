package GameState;

import java.util.List;

import GameObjects.Actor;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.MoveAction;
import RuleChecker.RuleChecker;

/**
 * Represents the game's current state and its rules.
 */
public interface GameState {

  /**
   * Creates the initial game state by placing the given actors in the game. Players
   * are placed in the first room, and adversaries are placed in the last room. The key is
   * placed on the given position
   *
   * @param players are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn is the position to place the key
   */
  void initGameState(List<Actor> players, List<Actor> adversaries, Posn keyPosn);


  /**
   * Modifies the game state after a player is expelled
   *
   * @param expelledPlayer
   */
  void handlePlayerExpulsion(Player expelledPlayer);


  /**
   * This method handles when player exits the game. Note: end game not implemented,
   * this method just removes the player from the game state and adds it to the exited
   * players.
   *
   * @param exitedPlayer
   */
  void handlePlayerExit(Player exitedPlayer);

  /**
   * Creates the initial game state by placing the given actors in the game. Players and adversaries
   * are placed where their positions are. The key is
   * placed on the given position
   *
   * @param players are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn is the position to place the key
   */
  void initGameStateWhereActorsHavePositions(List<Actor> players, List<Actor> adversaries, Posn keyPosn);

  /**
   *   GameObjects.Level object is created by random level generator in GameManager
   *   Also resets all the other fields according to the generated GameObjects.Level
   * @param newLevel
   */
  void setLevel(Level newLevel);

  /**
   *   Method determines if a player can move to the given location
   *   A move is valid if the tile is unoccupied/un-walled and near enough
   * @param actor
   * @param destination
   * @return
   */
  boolean validMove(Actor actor, Posn destination);

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


  boolean isLevelEnd();

  /**
   * Determines if the game state is a valid state.
   * @return
   */
  boolean isStateValid();

  /**
   * This returns the actors in the game.
   * @return
   */
  List<Actor> getActors();

  /**
   *
   * @return
   */
  Level getLevel();

  /**
   * This handles the given MoveAction object and uses the given rule checker to
   * validate any resulting interaction
   *
   * @param action
   * @param checker
   */
  void handleMoveAction(MoveAction action, RuleChecker checker);

  /**
   * Returns whether or not the current level is exitable
   *
   * @return
   */
  boolean isExitable();

  /**
   * This method returns all the tiles the user can see at its position
   *
   * @param userPosn the current position of the user
   * @return
   */
  List<List<Tile>> calculateVisibleTilesForUser(Posn userPosn);

  /**
   * This method a string interaction resulting from the player move
   * @param p
   * @param posn
   * @return
   */
  String handleMovePlayer(Player p, Posn posn);

  /**
   * Returns whether or not the player is currently on the exit in this game state.
   * @return
   */
  boolean isPlayerIsOnExit();
}
