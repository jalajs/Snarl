package User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import Action.Action;
import GameObjects.Actor;
import GameObjects.Posn;
import GameObjects.Tile;

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
   * This method returns a users surrounding tiles
   *
   * @return the surrounding tiles
   */
  List<List<Tile>> getSurroundings();

  /**
   * This method sets the visible tiles for the user
   *
   * @param surroundings are the visible tiles
   */
  void setSurroundings(List<List<Tile>> surroundings);

  /**
   * Updates the user with any changes that happened in the game state. This is only called in GameManager
   * @param updatedSurroundings the new surrounding of the user
   * @param isExitable whether or not the door has been unlocked
   * @param currentPosition where the player currently is
   */
  void update(List<List<Tile>> updatedSurroundings, boolean isExitable, Posn currentPosition, List<String> remainingPlayers, String event);

  /**
   * Prompts the user for its turn/move. The user does its move and returns an Action ack to the GameManager
   * @return the action object the user does from this move.
   */
  Action turn(Scanner scanner);

  /**
   * Finds the objects in the User's surroundings.
   * @return
   */
  List<Object> findObjects();

  /**
   * Finds the Actors in the User's surroundings.
   */
  List<Actor> findActors();

  /**
   * This method returns the ascii string representation of the visible tiles
   * @return the representation of the visible tiles
   */
  String visibleTileRepresentation();

  /**
   * Sets the currentPosition to the given Posn
   * @param posn
   */
  void setCurrentPosition(Posn posn);

  /*
   * Sets exitable to the given posn
   */
  void setExitable(boolean exitable);

  /**
   * Prompts the user for their name
   * @return A string, their name
   */
  String promptForName(Scanner scanner);

  void send(String message);

  String receive();

  int getNumExits();

  int getNumEjects();

  int getNumKeysCollected();

  void updateStats(boolean isEjected, boolean isExited, boolean isKeyFinder);
}
