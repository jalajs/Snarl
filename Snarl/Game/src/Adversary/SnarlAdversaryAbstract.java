package Adversary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Action.Action;
import GameObjects.Adversary;
import GameObjects.AdversaryType;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;

/**
 * This abstract class is the common representation for the snarl adversary and contains
 * common behavior between the adversary classes (Ghost and Zombie)
 */
public abstract class SnarlAdversaryAbstract implements SnarlAdversary {
  protected Level level;
  protected Posn currentPosition;
  protected Map<Posn, Adversary> adversaries;
  protected Random rand;
  protected String name;

  /**
   * Builds an abstract SnarlAdversary with the given level and current position
   * @param level
   * @param currentPosition
   */
  public SnarlAdversaryAbstract(Level level, Posn currentPosition){
    this.level = level;
    this.currentPosition = currentPosition;
    this.rand = new Random();
    this.name = "";
    this.adversaries = new HashMap<>();
  }

  /**
   * Builds an abstract SnarlAdversary with the given name
   * @param name
   */
  public SnarlAdversaryAbstract(String name){
    this.level = null;
    this.currentPosition = null;
    this.adversaries = new HashMap<>();
    this.rand = new Random();
    this.name = name;
  }

  /**
   * This is the turn method, different for both players and adversaries
   * @param players the locations of the players in the level
   * @param adversary the locations of the adversaries in the level
   * @return
   */
  abstract public Action turn(Map<Posn, Player> players, Map<Posn, Adversary> adversary);

  /**
   * This method updates the level and adversary posn map for this adversary. Same for both
   * Ghosts and Zombies
   * @param level the updated level
   * @param adversary the updated locations of other adversaries
   */
  @Override
  public void update(Level level, Map<Posn, Adversary> adversary) {
    this.level = level;
    this.adversaries = adversary;
  }

  /**
   * The distance formula
   * @param start
   * @param end
   * @return
   */
  protected double distance(Posn start, Posn end) {
    return Math.sqrt(
            Math.pow((start.getCol() - end.getCol()), 2) +
                    Math.pow((start.getRow() - end.getRow()), 2));
  }

  /**
   * This method uses the distance formula to find the move that brings the
   * adversary closest to the given player posn
   *
   * @param destinations
   * @param playerPosn
   * @return
   */
  protected Posn closestMoveToPlayer(List<Posn> destinations, Posn playerPosn) {
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

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  abstract public AdversaryType getType();

  @Override
  public void setCurrentPosition(Posn currentPosition) {
    this.currentPosition = currentPosition;
  }

  @Override
  public Posn getCurrentPosition() {
    return this.currentPosition;
  }
}
