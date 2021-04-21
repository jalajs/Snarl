import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Door;
import GameObjects.Collectable;
import GameObjects.ExitKey;
import GameState.GameStateModel;
import RuleChecker.RuleCheckerClass;


import static org.junit.Assert.*;

public class RuleCheckerTests {
  private final TestUtils testUtils = new TestUtils();

  /**
   * This test verifies that isLevelEnd correctly returns true in the two end level conditions
   *  1) all players are expelled
   *  2) the level is exitable and a player is on the exit
   *
   */
  @Test
  public void testisLevelEnd() {
    RuleCheckerClass ruleChecker = new RuleCheckerClass();
    Level expelledLevel = testUtils.createComplicatedLevel();
    GameStateModel expelledPlayersGameState = new GameStateModel(expelledLevel);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player();
    Player player2 = new Player();
    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    expelledPlayersGameState.initGameState(players, adversaries, new Posn(0, 9));
    assertFalse(ruleChecker.isLevelEnd(expelledPlayersGameState));

    List<Player> exitedPlayers = new ArrayList<>();
    exitedPlayers.add(player1);
    exitedPlayers.add(player2);
    expelledPlayersGameState.setExitedPlayers(exitedPlayers);

    //tests first condition
    assertTrue(ruleChecker.isLevelEnd(expelledPlayersGameState));

    Level exitLevel = testUtils.createComplicatedLevel();
    GameStateModel exitGameState = new GameStateModel(exitLevel);
    exitGameState.initGameState(players, adversaries, new Posn(0, 9));

    assertFalse(ruleChecker.isLevelEnd(exitGameState));
    exitGameState.setExitable(true);
    exitGameState.setPlayerIsOnExit(true);

    assertTrue(ruleChecker.isLevelEnd(exitGameState));
  }

  /**
   * This method tests that when the final level ends, the isGameEnd is true
   * the GameState keeps track of the current level and final level. The game is complete if
   * the current level is the final level and the isLevelEnd is true
   */
  @Test
  public void testIsGameEnd() {
    RuleCheckerClass ruleChecker = new RuleCheckerClass();
    Level lastLevel = testUtils.createComplicatedLevel();
    GameStateModel endGameState = new GameStateModel(lastLevel);

    endGameState.setCurrentLevelNumber(1);
    endGameState.setTotalLevels(3);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player();
    Player player2 = new Player();
    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    endGameState.initGameState(players, adversaries, new Posn(0, 9));

    assertFalse(ruleChecker.isGameEnd(endGameState));
    endGameState.setExitable(true);
    endGameState.setPlayerIsOnExit(true);

    assertFalse(ruleChecker.isGameEnd(endGameState));

    endGameState.setCurrentLevelNumber(3);

    assertTrue(ruleChecker.isGameEnd(endGameState));
  }

  /** A GameState is invalid if any
   * actors are on walls or otherwise in bad positions or if the exit key is in a bad position
   */
  @Test
  public void testIsGameStateValid() {
    RuleCheckerClass ruleChecker = new RuleCheckerClass();
    Level level = testUtils.createComplicatedLevel();
    GameStateModel gameState = new GameStateModel(level);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player();
    Player player2 = new Player();
    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    // place a player in a bad position (non-traversable wall)
    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    gameState.initGameState(players, adversaries, new Posn(0, 9));
    assertTrue(ruleChecker.isGameStateValid(gameState));
    // move player to a bad position
    player1.setPosition(new Posn(1,2));
    assertFalse(ruleChecker.isGameStateValid(gameState));
    // move player back to an ok position
    player1.setPosition(new Posn(0,0));
    assertTrue(ruleChecker.isGameStateValid(gameState));
    // move key to a bad position
    level.setExitKeyPosition(new Posn(1, 2));
    assertFalse(ruleChecker.isGameStateValid(gameState));
  }

  /**
   * A move is invalid if it is too far, wall, empty space (no tile)
   * A move is to far if the desired tile is more than two cardinal moves away
   *
   */
  @Test
  public void testIsMoveValid() {
    RuleCheckerClass ruleChecker = new RuleCheckerClass();
    Level level = testUtils.createComplicatedLevel();
    level.initGrid();
    Actor player1 = new Player();
    player1.setPosition(new Posn(0, 0));
    assertTrue(ruleChecker.isMoveValid(level, player1, new Posn(1, 0)));
    assertFalse(ruleChecker.isMoveValid(level, player1, new Posn(3, 3)));
    assertFalse(ruleChecker.isMoveValid(level, player1, new Posn(1, 2)));
  }

  /**
   * A player cannot interact with a door that is an exit if they do not have a key
   * NOR with another player
   */
  @Test
  public void testIsInteractionValid() {
    RuleCheckerClass ruleChecker = new RuleCheckerClass();
    Actor player = new Player();
    Tile exitTile = new Tile(false);
    Door exit = new Door();
    exit.setLevelExit(true);
    exitTile.setDoor(exit);
    assertFalse(ruleChecker.isInteractionValid(false, player, exitTile));
    assertTrue(ruleChecker.isInteractionValid(true, player, exitTile));

    Tile anotherPlayerTile = new Tile(false);
    Actor anotherPlayer = new Player();
    anotherPlayerTile.setOccupier(anotherPlayer);
    assertFalse(ruleChecker.isInteractionValid(false, player, anotherPlayerTile));
    assertFalse(ruleChecker.isInteractionValid(true, player, anotherPlayerTile));

    Tile collectableTile = new Tile(false);
    Collectable key = new ExitKey(new Posn(0,0));
    collectableTile.setCollectable(key);
    assertTrue(ruleChecker.isInteractionValid(false, player, collectableTile));
  }
}
