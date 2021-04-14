import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import Action.MoveAction;
import Adversary.Zombie;
import Adversary.Ghost;
import GameObjects.Actor;
import GameObjects.Level;
import GameObjects.Adversary;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * This testing suite verifies all the movement logics for the adversaries
 */
public class AdversaryTest {
  private final TestUtils testUtils = new TestUtils();

  /**
   * A zombie can move at most one tile at a time in a cardinal direction. A zombie cannot skip a
   * move, unless there is no valid move in any cardinal direction. A zombie cannot move onto a tile
   * occupied by another adversary. A zombie cannot move onto a door tile. A consequence is, it
   * cannot leave the room it spawned in.
   *
   */
  @Test
  public void testZombieTurn() {
    Level level = testUtils.createComplicatedLevel();
    level.initGrid();
    Posn zombieStartingPosn = new Posn(2, 1);
    Zombie zombie = new Zombie(level, zombieStartingPosn);

    List<Posn> possibleZombieMoves = zombie.calculatePossibleDestinations();

    assertEquals(2, possibleZombieMoves.size());
    assertTrue(possibleZombieMoves.contains(new Posn(2, 0)));
    assertTrue(possibleZombieMoves.contains(new Posn(1, 1)));

    zombie.setRand(new Random(2));
    MoveAction action = (MoveAction) zombie.turn(new HashMap<>(), new HashMap<>());
    assertEquals(new Posn(2, 0), action.getDestination());

    Map<Posn, Adversary> adversaryMap = new HashMap<>();
    adversaryMap.put(new Posn(2, 0), new Adversary());
    adversaryMap.put(new Posn(1, 1), new Adversary());

    // if surrounded by walls, doors, and or adversaries, the zombie will do nothing
    MoveAction nothingMove = (MoveAction) zombie.turn(new HashMap<>(), adversaryMap);
    assertTrue(nothingMove.getCurrentPosition().equals(nothingMove.getDestination()));

    // testing that a zombie moves to a player when they are close
    Posn frankPoint = new Posn(0, 7);
    Zombie frank = new Zombie(level, frankPoint);
    Actor player = new Player();
    player.setPosition(new Posn(1, 7));
    List<Actor> players = new ArrayList<>();
    players.add(player);
    level.placeActorsInLevel(players);

    List<Posn> possibleFrankMoves = frank.calculatePossibleDestinations();

    assertEquals(2, possibleFrankMoves.size());
    assertTrue(possibleFrankMoves.contains(new Posn(0, 8)));
    assertTrue(possibleFrankMoves.contains(new Posn(1, 7)));

    Map<Posn, Player> playerMap = new HashMap<>();
    playerMap.put(new Posn(1, 7), (Player) player);
    MoveAction actionF = (MoveAction) frank.turn(playerMap, new HashMap<>());
    assertEquals(new Posn(1, 7), actionF.getDestination());

  }


  /**
   * ...    ...
   * ..X    ...
   * ..|....|.|
   *          .
   *          .
   *          .
   * ...|.....+
   * XX.X
   * ....
   * ...|
   *    .
   *    .
   *    .
   *    +...|..
   *        ..|
   * A ghost can move at most one tile at a time in a cardinal direction. A ghost cannot skip a
   * move, unless there is no valid move in any cardinal direction. If the ghost enters a wall tile
   * of a room, it triggers an interaction whereby it is transported to a randomly selected room. A
   * ghost can enter a door tile and move through hallways.
   */
  @Test
  public void testGhostTurn() {
    Level level = testUtils.createComplicatedLevel();
    level.initGrid();
    Posn ghostStartingPosn = new Posn(1, 1);
    Ghost casper = new Ghost(level, ghostStartingPosn);
    casper.setRand(new Random());
    List<Posn> allPossibleMoves = level.getCardinalMoves(ghostStartingPosn);
    assertEquals(4, allPossibleMoves.size());
    MoveAction moveAction1 = (MoveAction) casper.turn(new HashMap<>(), new HashMap<>());

    // make sure the ghost moved to the wall tile (1, 2) and generated in a room that is not
    // the first room. If this passes the transport logic is verified!
    assertFalse(level.getRooms().get(0).isPosnInRoom(moveAction1.getDestination()));

    // testing that a ghost moves to a player when they are close
    Posn frankPoint = new Posn(0, 7);
    Ghost frank = new Ghost(level, frankPoint);
    Actor player = new Player();
    player.setPosition(new Posn(1, 7));
    List<Actor> players = new ArrayList<>();
    players.add(player);
    level.placeActorsInLevel(players);

    Map<Posn, Player> playerMap = new HashMap<>();
    playerMap.put(new Posn(1, 7), (Player) player);
    MoveAction actionF = (MoveAction) frank.turn(playerMap, new HashMap<>());
    assertEquals(new Posn(1, 7), actionF.getDestination());
  }
}
