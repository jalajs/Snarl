package GameManager;

import GameObjects.Level;
import GameObjects.Action;

/**
 * The GameManager is tasked with sequencing the game. It must register players and adversaries, update
 * the human players of the game as things change, execute actions on the gameState, promptTurns, and
 * manage all other components of game play.
 *
 */
public interface GameManager {

  /**
   *  This method takes in the name of the new player and registers it as a User to the game
   * @param name
   *
   */
  void addPlayer(String name);

  /**
   * This method takes in the string type of an adversary and registers it to the game
   * @param type
   *
   */
  void addAdversary(String type, String name);


  /**
   * This method starts the game by initializing the GameState.
   * This entails setting the level and creating Player objects to represent each User object
   * @param level the level to start the game with
   */
  void startGame(Level level);

  /**
   * This method iterates through the Users and sends them each the current information on the
   * GameState.
   *
   */
  void updateUsers();

  /**
   * This method prompts the correct Actor to take a turn by calling the correct User's turn method
   * user.turn() returns an Action, which is then passed into executeAction
   * After the action is executed, each User is given updated information on the GameState using the method updatePlayerView()
   * An example of this is if one User moves into other Users' visible tiles, than we know that their visible tiles must be updated
   * The turn int is upped after every call
   * If it is an Adversary's turn, it's automated turn code is called.
   */
  void promptPlayerTurn();

  /**
   * Execute action performs the given action on the GameState
   * @param action
   * @param actorType indicates the type of actor making the move
   */
  void executeAction(String actorType, Action action);
}
