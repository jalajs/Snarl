package User;
import java.util.ArrayList;
import java.util.List;

import GameObjects.Action;

import GameObjects.Posn;
import GameObjects.Tile;
import GameState.GameState;

public class UserClass implements User {
  private String name;
  private List<List<Tile>> surroundings;
  private Posn currentPosition;


  public UserClass(String name) {
    this.name = name;
    this.surroundings = new ArrayList<>();
    this.currentPosition = new Posn(0, 0);
  }

  /**
   * Updates the user with any changes that happened in the game state. This is only called in GameManager
   * @param updatedSurroundings the new surrounding of the user
   * @param isExitable whether or not the door has been unlocked
   */
  @Override
  public void update(List<List<Tile>> updatedSurroundings, boolean isExitable) {

  }

  /**
   * Prompts the user for its turn/move. The user does its move and returns an Action ack to the GameManager
   * @return the action object the user does from this move.
   */
  @Override
  public Action turn() {
    return null;
  }


  @Override
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<List<Tile>> getSurroundings() {
    return surroundings;
  }

  public void setSurroundings(List<List<Tile>> surroundings) {
    this.surroundings = surroundings;
  }

  public Posn getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(Posn currentPosition) {
    this.currentPosition = currentPosition;
  }
}
