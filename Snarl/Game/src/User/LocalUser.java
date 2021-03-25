package User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Action.Action;

import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Door;
import GameObjects.Actor;

/**
 * This represents the logics behind a local player (i.e. one operating through the local console)
 */
public class LocalUser implements User {
  private String name;
  // this is a List instead of a Tile[][] because its possible the amount of tiles a player can
  // see will change as the level progresses
  private List<List<Tile>> surroundings;
  private Posn currentPosition;
  private boolean isExitable;

  /**
   * This builds a LocalUser given a name
   *
   * @param name
   */
  public LocalUser(String name) {
    this.name = name;
    this.surroundings = new ArrayList<>();
    this.currentPosition = new Posn(0, 0);
    this.isExitable = false;
  }

  /**
   * Updates the user with any changes that happened in the game state. This is only called in
   * GameManager
   *
   * @param updatedSurroundings the new surrounding of the user
   * @param isExitable          whether or not the door has been unlocked
   * @param currentPosition     the current position of the user
   */
  @Override
  public void update(List<List<Tile>> updatedSurroundings, boolean isExitable, Posn currentPosition) {
    this.currentPosition = currentPosition;
    this.surroundings = updatedSurroundings;
    this.isExitable = isExitable;
    this.renderView();
  }

  /**
   * Renders an updated view to the user on the console.
   */
  private void renderView() {
    String tileString = this.visibleTileRepresentation();
    String exitStatus = isExitable ? "open" : "closed";
    System.out.println("Current position in the level: " +
            "(" + this.currentPosition.getRow() + ", " + this.currentPosition.getCol() + ")");
    System.out.println("The exit is " + exitStatus);
    System.out.println(tileString);
  }

  /**
   * Creates a representation for the visible tiles the user can see, including the user's current
   * position.
   *
   * @return A string representation of the user's current state
   */
  public String visibleTileRepresentation() {
    String roomAcc = "";
    for (int i = 0; i < this.surroundings.size(); i++) {
      for (int j = 0; j < this.surroundings.get(i).size(); j++) {
        Tile tile = this.surroundings.get(i).get(j);
        if (tile == null) {
          roomAcc += " ";
        }
        else {
          roomAcc += this.surroundings.get(i).get(j).toString();
        }
      }
      if (i != this.surroundings.size() - 1) {
        roomAcc += "\n";
      }
    }
    return roomAcc;
  }

  /**
   * Prompts the user for its turn/move. The user does its move and returns an Action back to the
   * GameManager
   *
   * @return the action object the user does from this move.
   */
  @Override
  public Action turn() {
    Posn newPosition = this.promptUserForTurn();

    Tile newTile = this.findTile(newPosition);
    newTile.setPosition(newPosition);

    return newTile.buildAction(this.currentPosition, this.name);
  }

  /**
   * This method prompts the user for coordinates to move to for their turn. The user
   *  should provide coordinates relevant to their visible tiles to make it easier.
   *
   * @return the players new position
   */
  private Posn promptUserForTurn() {
    Posn posn = new Posn(-1, -1);
    Scanner scanner = new Scanner(System.in);
    while(!isPosnValid(posn)) {
      this.renderView();
      System.out.println("Please enter the desired coordinates for your move in the form row col all on the same line");
      System.out.println("If your move is invalid, you will be prompted for another coordinate");

      int row = scanner.nextInt();
      int col = scanner.nextInt();

      posn = new Posn(row, col);
    }
    scanner.close();
    return posn;
  }

  /**
   * Parses the given string into a Posn
   *
   * @param posnString the string to be parsed example posn string = 1 2
   * @return a posn with the coordinates
   */
  public Posn createPosn(String posnString) {
    String[] stringArray = posnString.split(" ");
    int row = Integer.parseInt(stringArray[0]);
    int col = Integer.parseInt(stringArray[1]);
    return new Posn(row, col);
  }

  /**
   * Checks whether or not the given Posn is valid.
   *
   * @param posn the posn being checked
   * @return A posn is valid if it is within the surroundings of the user AND traversable
   */
  private boolean isPosnValid(Posn posn) {
    int row = posn.getRow();
    int col = posn.getCol();
    int rowLength = this.surroundings.size();
    int colLength = this.surroundings.get(0).size();
    if ((row < rowLength && row >= 0) && (col < colLength && col >= 0)) {
      Tile tile = this.surroundings.get(row).get(col);
      if (tile.getDoor() != null) {
        return !tile.getDoor().isLevelExit() || (tile.getDoor().isLevelExit() && isExitable);
      }
      if (tile.getOccupier() != null) {
        return !tile.getOccupier().isPlayer();
      } else {
        return !tile.getisWall();
      }
    } else {
      return false;
    }
  }

  /**
   * Find the tile with the given coordinates in this user's surroundings.
   *
   * @param newPosition the position of the tile the user is moving to
   * @return the Tile the user is moving to
   */
  private Tile findTile(Posn newPosition) {
    int row = newPosition.getRow();
    int col = newPosition.getCol();
    return this.surroundings.get(row).get(col);
  }


  /**
   * Finds the objects in the User's surroundings and returns them in a list
   * @return the list of objects in a user's surroundings
   */
  @Override
  public List<Object> findObjects() {
    List<Object> objects = new ArrayList<>();
    for (int i = 0; i < this.surroundings.size(); i++) {
      for (int j = 0; j < this.surroundings.get(i).size(); j++) {
        if (this.surroundings.get(i).get(j) != null) {
          Tile tile = this.surroundings.get(i).get(j);
          Door door = tile.getDoor();
          if (tile.getCollectable() != null) {
            objects.add(tile.getCollectable());
          }
          if (door != null && door.isLevelExit()) {
            objects.add(door);
          }
        }
      }
    }
    return objects;
  }

  /**
   * Finds the actors in the User's surroundings and returns them in a list
   * @return the list of actors in a user's surroundings
   */
  @Override
  public List<Actor> findActors() {
    List<Actor> actors = new ArrayList<>();
    for (int i = 0; i < this.surroundings.size(); i++) {
      for (int j = 0; j < this.surroundings.get(i).size(); j++) {
        if (surroundings.get(i).get(j) != null) {
          Tile tile = surroundings.get(i).get(j);
          Actor occupier = tile.getOccupier();
          if (occupier != null && !(i == 2 && j == 2)) {
            actors.add(occupier);
          }
        }
      }
    }
    return actors;
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

  public boolean isExitable() {
    return isExitable;
  }

  public void setExitable(boolean exitable) {
    isExitable = exitable;
  }
}
