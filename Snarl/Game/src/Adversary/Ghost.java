package Adversary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Action.Action;
import Action.MoveAction;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;

/**
 * This class represents a ghost adversary
 */
public class Ghost extends SnarlAdversaryAbstract {
  private static int searchRadius = 4;

  public Ghost(Level level, Posn currentPosition) {
    super(level, currentPosition);
  }

  public Ghost(String name) {
    super(name);
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
    if (destinations.size() > 0) {
      Posn destination = this.getBestMove(players, destinations);
      // return the action to the gameManager
      this.currentPosition = destination;
      return new MoveAction(destination, this.currentPosition);
    }
    return new MoveAction(this.currentPosition, this.currentPosition);
  }

  /**
   * - move towards closest player in the radius
   * - if no player with in certain radius, teleport through wall if next to one, else pick random move
   * - if player within radius move in direction of player and avoid walls
   * @param players map of player locations surrounding the user
   * @param destinations  list of potential destinations
   * @return
   */
  private Posn getBestMove(Map<Posn, Player> players, List<Posn> destinations) {
    List<Posn> nonWallDestinations = this.getNonWallDestinations(destinations);
    Posn destination = new Posn(-100, -100);
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
        Posn transportPosition = this.level.generateTransportPosition(currentPosition);
        return transportPosition;
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
