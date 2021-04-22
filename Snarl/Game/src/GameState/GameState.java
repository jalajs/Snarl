package GameState;

import java.util.List;
import java.util.Map;

import Action.InteractionType;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;
import Action.Action;
import Action.MoveAction;
import RuleChecker.RuleChecker;
import Adversary.SnarlAdversary;

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
   * This method simply initializes the level grid. We created it so that we can initialize
   * the level grid at the GS level from the test harnesses.
   *
   */
  void initLevelGrid();


  /**
   * Modifies the game state after a player is expelled
   *
   * @param expelledPlayer the player being expelled
   * @param oldPosition the position of where the player was
   */
  void handlePlayerExpulsion(Player expelledPlayer, Posn oldPosition);

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
   * @return boolean returns true if the move was executed
   */
  boolean handleMoveAction(MoveAction action, RuleChecker checker);

  /**
   * Returns whether or not the current level is exitable
   *
   * @return
   */
  boolean isExitable();

  /**
   * This method a string interaction resulting from the player move
   * @param p
   * @param posn
   * @return
   */
  InteractionType calculateInteractionType(Player p, Posn posn);

  /**
   * This method handles a very simple move performed by an adversary that has already been
   * verified before this step. Ergo, no rule checker is needed.
   *
   * @param action the given move action
   * @return
   */
  boolean handleMoveAction(MoveAction action, SnarlAdversary adversary);

  /**
   * Returns whether or not the player is currently on the exit in this game state.
   * @return
   */
  boolean isPlayerIsOnExit();

  /**
   * Modifies the game state after a player/adversary collects exit key
   * @param player the player who collected the key
   */
  void handleKeyCollection(Player player, MoveAction action);

  /**
   * Get the surrounding tiles around the given posn
   * @param position the posn in the center of the layout returned
   * @return a 5x5 layout of tiles
   */
  List<List<Tile>> getSurroundingsForPosn(Posn position);

  /**
   * Get the players extied from the games. Exited
   * @return a List of players exited from the games=.
   */
  List<Player> getExitedPlayers();

  /**
   * Get the players  who were ejected from the games.  expelled
   * @return a List of players ejected from the games.
   */
  List<Player> getEjectedPlayers();

  /**
   * This method drops players and adversaries randomly in the level
   * @param players
   * @param adversaries
   * @param keyPosn
   * @return a map associatning the players names to their initial positions
   */
  Map<String, Posn> initLocalGameState(List<Actor> players, List<Actor> adversaries, Posn keyPosn);

  /**
   * Gets particular actors from the game state
   * @param isPlayer if this method should get only the players or only the adversaries
   * @return a list of actors containing only the type specified
   */
  List<Actor> getActors(boolean isPlayer);

  /**
   * Set the current level the game state is representing
   * @param currentLevelNumber the number of the current level to be set
   */
  void setCurrentLevelNumber(int currentLevelNumber);

  /**
   * Gets the current level number in the game
   * @return the current level
   */
  int getCurrentLevelNumber();

  /**
   * set the total number of levels
   * @param totalLevels
   */
  void setTotalLevels(int totalLevels);

  /**
   * Gets the total number of levels in the game
   * @return
   */
  int getTotalLevels();

  /**
   * Calculates the number of players in the list of actors
   *
   * @return the number of players
   */
   int numberOfPlayers();

  /**
   * Returns the player who found the key in this game
   * @return null, if no player has found the key
   */
  Player getKeyFinder();

  /**
   * This method handles the move action produced by a players move.
   *
   * @param player
   * @param action
   */
  void handleInteractionType(Player player, MoveAction action);

  /**
   * This method handles the move action produced by an adversary move. Note, it
   * is an overloaded version of handleInteractionType(player, action).
   * @param adversary
   * @param action
   */
  void handleInteractionType(Adversary adversary, MoveAction action);
}
