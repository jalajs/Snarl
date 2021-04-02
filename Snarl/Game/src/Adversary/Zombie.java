package Adversary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import Action.Action;
import Action.MoveAction;
import Action.DoNothingAction;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;

/**
 * This class represents a zombie adversary
 */
public class Zombie extends SnarlAdversaryAbstract {
  private static final int searchRadius = 3;

  /**
   * this constructor builds a zombie with a given level and position
   * @param level
   * @param currentPosition
   */
  public Zombie(Level level, Posn currentPosition){
    super(level, currentPosition);
  }

  /**
   * This constructor creates
   * @param name
   */
  public Zombie(String name) {
    super(name);
  }

  /**
   * Prompt the adversary for a turn and return the action to the game manager
   * @param players the locations of the players in the level
   * @param adversaryMap the locations of the adversaries in the level
   * @return
   */
  @Override
  public Action turn(Map<Posn, Player> players, Map<Posn, Adversary> adversaryMap) {
    this.adversaries = adversaryMap;
    // calculate the possible tiles it can move to (so 1 cardinal move (no skip)/no door/ no adversary)
    List<Posn> destinations = this.calculatePossibleDestinations();
    //  pick one randomly
    if (destinations.size() > 0) {
      // this employs our strategies to select the best possible move
      Posn destination = this.getBestMove(players, destinations);
      // return the action to the gameManager
      this.currentPosition = destination;
      return new MoveAction(destination, this.currentPosition);
    }
    return new MoveAction(this.currentPosition, this.currentPosition);
  }

  /**
   * This method returns the best move for the zombie out of the given legal destinations
   * by employing our zombie strategies. The strategies are described in the adversaries-strategies.md
   *
   * @param players
   * @param destinations
   * @return
   */
  public Posn getBestMove(Map<Posn, Player> players, List<Posn> destinations) {
    Posn destination = destinations.get(rand.nextInt(destinations.size()));
    // if player in radius, chase player. else, just wander randomly
    for (Map.Entry<Posn,Player> entry : players.entrySet()) {
      Posn posn = entry.getKey();
      if (distance(currentPosition, posn) <= searchRadius) {
        destination = closestMove(destinations, posn);
      }
    }
    return destination;
  }


  /**
   * This method uses the distance formula to find the move that brings the
   * zombie closest to the given player posn
   *
   * @param destinations
   * @param playerPosn
   * @return
   */
  private Posn closestMove(List<Posn> destinations, Posn playerPosn) {
    Posn destination = destinations.get(0);
    double minDistance = distance(playerPosn, destination);
    for (Posn posn : destinations) {
      if (playerPosn.equals(posn)) {
        return posn;
      }
      if (distance(playerPosn, posn) < minDistance && !posn.equals(currentPosition)) {
         destination = posn;
         minDistance = distance(playerPosn, posn);
      }
    }
    return destination;
  }


  /**
   * Calculates the possible destinations the zombie can move. First all the possible cardinal moves
   * within the level are calculated. Then, moves that are illegal for zombies (they land on walls,
   * doors, or other adversaries) are removed from all the moves.
   *
   * @return The mutated moves list
   */
  public List<Posn> calculatePossibleDestinations() {
    List<Posn> allPossibleMoves = this.level.getCardinalMoves(this.currentPosition);
    List<Posn> verifiedMoves = new ArrayList<>();
    Tile[][] levelTileGrid = this.level.getTileGrid();
    for (Posn move : allPossibleMoves) {
      Tile tile = levelTileGrid[move.getRow()][move.getCol()];
      if (!tile.isWall() && tile.getDoor() == null &&
              adversaries.getOrDefault(move, null) == null) {
        verifiedMoves.add(move);
      }
    }
    return verifiedMoves;
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

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return "Zombie";
  }
}
