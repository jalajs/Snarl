package Adversary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Action.Action;
import Action.DoNothingAction;
import Action.MoveAction;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;

/**
 * This class represents a ghost adversary
 */
public class Ghost implements SnarlAdversary {
  private Level level;
  private Posn currentPosition;
  private Map<Posn, Adversary> adversaries;
  private static int searchRadius = 4;
  private String name;

  private Random rand;

  public Ghost(Level level, Posn currentPosition) {
    this.level = level;
    this.currentPosition = currentPosition;
    this.adversaries = new HashMap<>();
    this.rand = new Random();
    this.name = "";
  }

  public Ghost(String name) {
    this.level = null;
    this.currentPosition = null;
    this.adversaries = new HashMap<>();
    this.rand = new Random();
    this.name = name;
  }


  /**
   * A ghost can move at most one tile at a time in a cardinal direction. A ghost cannot skip a
   * move, unless there is no valid move in any cardinal direction. If the ghost enters a wall tile
   * of a room, it triggers an interaction whereby it is transported to a randomly selected room. A
   * ghost can enter a door tile and move through hallways.
   *
   * @param players   the locations of the players in the level
   * @param adversaryMap the locations of the adversaries in the level
   * @return
   */
  @Override
  public Action turn(Map<Posn, Player> players, Map<Posn, Adversary> adversaryMap) {
    this.adversaries = adversaryMap;
    List<Posn> destinations = this.level.getCardinalMoves(this.currentPosition);
    //  pick one randomly
    if (destinations.size() > 0) {
      Posn destination = this.getBestMove(players, destinations);
      // return the action to the gameManager
      return new MoveAction(destination, this.currentPosition);
    }
    return new DoNothingAction();
  }

  /**
   * - move towards closest player in the radius
   * - if no player with in certain radius, teleport through wall if next to one, else move through wall (move towards wall)
   * - if player within radius move in direction of player and avoid walls
   * @param players
   * @param destinations
   * @return
   */
  private Posn getBestMove(Map<Posn, Player> players, List<Posn> destinations) {
    List<Posn> nonWallDestinations = this.getNonWallDestinations(destinations);
    Posn destination = new Posn(-100, -100);
    // if player in radius, chase player. else, go to nearest wall
    for (Map.Entry<Posn,Player> entry : players.entrySet()) {
      Posn posn = entry.getKey();
      if (distance(currentPosition, posn) <= searchRadius) {
        destination = closestMoveToPlayer(nonWallDestinations, posn);
      }
    }
    if (distance(destination, currentPosition) <= searchRadius) {
      return destination;
    }
    return this.closestMoveToWall(destinations);
  }

  /**
   * This method either returns a the position after transporting if a wall is close or picks a move randomly.
   * It does this by first iterating through the given destinations to check for walls. If none are walls, the move is selected randomly
   * from the given destinations.
   *
   * @param destinations the given destinations
   * @return the destination the ghost chooses
   */
  private Posn closestMoveToWall(List<Posn> destinations) {
    Tile[][] tileGrid = level.getTileGrid();
    List<Posn> validDestinations = new ArrayList<>();
    for (Posn posn : destinations) {
      int row = posn.getRow();
      int col = posn.getCol();
      Tile tile = tileGrid[row][col];
      if (tile.isWall() && !tile.isNothing()) {
        return this.level.generateTransportPosition(currentPosition);
      }
      else if (!tile.isNothing()) {
        validDestinations.add(posn);
      }
    }
    return validDestinations.get(rand.nextInt(validDestinations.size()));
  }



  /**
   * Returns all the nonwalled tiles that the ghost could move too
   *
   * @param destinations all the possible destinations
   * @return
   */
  private List<Posn> getNonWallDestinations(List<Posn> destinations) {
    List<Posn> nonWallDestinations = new ArrayList<>();
    Tile[][] levelTileGrid = this.level.getTileGrid();
    for (Posn possibleDestination : destinations) {
      int row = possibleDestination.getRow();
      int col = possibleDestination.getCol();
      Tile tile = levelTileGrid[row][col];
      if (!tile.isWall())  {
        nonWallDestinations.add(possibleDestination);
      }
    }
    return nonWallDestinations;
  }

  /**
   * This method uses the distance formula to find the move that brings the
   * ghost closest to the given player posn
   *
   * @param destinations
   * @param playerPosn
   * @return
   */
  private Posn closestMoveToPlayer(List<Posn> destinations, Posn playerPosn) {
    Posn destination = destinations.get(0);
    double minDistance = distance(playerPosn, destination);
    for (Posn posn : destinations) {
      if (distance(playerPosn, posn) < minDistance) {
        destination = posn;
        minDistance = distance(playerPosn, posn);
      }
    }
    return destination;
  }

  /**
   * The distance formula
   * @param start
   * @param end
   * @return
   */
  private double distance(Posn start, Posn end) {
    return Math.sqrt(
            Math.pow((start.getCol() - end.getCol()), 2) +
                    Math.pow((start.getCol() - end.getCol()), 2));
  }


  /**
   * This method updates all of the ghost's game information
   * @param level the updated level
   * @param adversary the updated locations of other adversaries
   */
  @Override
  public void update(Level level, Map<Posn, Adversary> adversary) {
    this.level = level;
    this.adversaries = adversary;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public Posn getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(Posn currentPosition) {
    this.currentPosition = currentPosition;
  }

  public Map<Posn, Adversary> getAdversaries() {
    return adversaries;
  }

  public void setAdversaries(Map<Posn, Adversary> adversaries) {
    this.adversaries = adversaries;
  }

  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }

  public String getType() {
    return "Ghost";
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
