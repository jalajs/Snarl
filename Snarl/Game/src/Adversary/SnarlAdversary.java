package Adversary;

import java.util.Map;

import Action.Action;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;


/**
 * Interface for how adversaries interact with the game SNARL
 */
public interface SnarlAdversary {

  /**
   * Prompt the adversary for a turn and return the action to the GameManager
   * @param players the locations of the players in the level
   * @param adversary the locations of the adversaries in the level
   * @return the turn the adversary makes in the game
   */
  Action turn(Map<Posn, Player> players, Map<Posn, Adversary> adversary);

  /**
   * This method updates all the information an Adversary has about the level, including the Level itself and the other adversary locations
   * @param level the updated level
   * @param adversary the updated locations of other adversaries
   */
  void update(Level level, Map<Posn, Adversary> adversary);

  /**
   * Gets the name of the SnarlAdversary
   * @return the name
   */
  String getName();

  /**
   * Gets type of adversary as a String
   * @return the type
   */
  String getType();

  /**
   * Sets the current position
   */
  void setCurrentPosition(Posn currentPosition);

}
