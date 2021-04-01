package GameManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GameObjects.Level;
import Action.Action;
import GameObjects.Posn;
import GameState.GameState;
import User.User;

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
   * This method starts a local game by initializing the GameState.
   * This entails setting the level and creating Player objects to represent each User object
   * @param level the level to start the game with
   */
  boolean startLocalGame(Level level, int levelNumber);

  /**
   * Starts the game trace for the testing task.
   *
   * @param level the level to start the game with
   * @param posnList the list of intital positions for actors.
   *              The first n positions are for the users and any subsequent posn is for an Adversary we create
   */
  void startGameTrace(Level level, List<Posn> posnList);

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
  void promptPlayerTurn(Scanner scanner);

  /**
   * Execute action performs the given action on the GameState
   * @param action
   * @param actorType indicates the type of actor making the move
   */
  void executeAction(String actorType, Action action, User user);

  /**
   * Returns the user with the given name
   * @param name the String representing the user's name
   * @return the user with the given name. null if a user does not exist with that name
   */
  User getUserByString(String name);

  /**
   * Play out the move of the User's whose turn it is.
   * @param managerTrace the JSONArray to add traces to
   * @return whether or not all moves have been used
   */
  boolean playOutMove(JSONArray managerTrace);

  /**
   * Set the move input stream to the given list of list of positions
   * @param actorMoveListList the positions represent the destination for each move
   */
  void setMoveInput(ArrayList<ArrayList<Posn>> actorMoveListList);

  /**
   * Get the remaining players still in the game
   * @return A list of the names of the players remaining
   */
  List<String> getRemainingPlayers();

  /**
   * Get the game state from the game manager.
   * @return the current game state of the game.
   */
  GameState getGs();

  void runLocalGame();
}
