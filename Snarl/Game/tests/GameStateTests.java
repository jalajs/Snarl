import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameState.GameStateModel;

import static org.junit.Assert.*;

public class GameStateTests {
  private final TestUtils testUtils = new TestUtils();

  /**
   * This method tests that init gameState places the characters into the level and otherwise
   * initializes the game.
   */
  @Test
  public void testInitGameState() {
    Level initLevel = testUtils.createComplicatedLevel();
    GameStateModel simpleGameState = new GameStateModel(initLevel);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player();
    Player player2 = new Player();
    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    assertEquals(simpleGameState.getActors(), new ArrayList<>());
    assertEquals(simpleGameState.getCollectables(), new ArrayList<>());

    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    simpleGameState.initGameState(players, adversaries, new Posn(0, 9));

    assertEquals(simpleGameState.getLevel().createLevelString(), "OO.    ..K\n" +
            "..X    ...\n" +
            "..|....|.|\n" +
            "         .\n" +
            "         .\n" +
            "         .\n" +
            "...|.....+\n" +
            "XX.X      \n" +
            "....      \n" +
            "...|      \n" +
            "   .      \n" +
            "   .      \n" +
            "   .      \n" +
            "   +...|##\n" +
            "       ..|");
  }


  /**
   * This method tests that when handleKeyCollection is called, the key is removed from the
   * gameState's level and isExitable is set to true/
   */
  @Test
  public void testHandleKeyCollection() {
    Level initLevel = testUtils.createComplicatedLevel();
    GameStateModel simpleGameState = new GameStateModel(initLevel);

    assertFalse(simpleGameState.isExitable());
    assertFalse(initLevel.createLevelString().contains("K"));

    simpleGameState.initGameState(new ArrayList<>(), new ArrayList<>(), new Posn(0, 0));

    assertFalse(simpleGameState.isExitable());
    assertTrue(initLevel.createLevelString().contains("K"));

    simpleGameState.handleKeyCollection();

    assertTrue(simpleGameState.isExitable());
    assertFalse(initLevel.createLevelString().contains("K"));
  }


  @Test
  public void testIntermediateGameState() {
    GameStateModel simpleGameState = new GameStateModel(testUtils.createComplicatedLevel());

    assertEquals(simpleGameState.getActors(), new ArrayList<>());
    assertEquals(simpleGameState.getCollectables(), new ArrayList<>());

    Player player1 = new Player();
    Player player2 = new Player();

    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    List<Actor> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);

    List<Actor> adversaries = new ArrayList<>();
    adversaries.add(adversary1);
    adversaries.add(adversary2);
    simpleGameState.initGameState(players, adversaries, new Posn(0, 9));

    assertEquals(simpleGameState.getActors().size(), 4);

    Posn newPosition = new Posn(1, 1);
    Posn newPosition2 = new Posn(2, 2);
    List<Posn> newPlayerPositions = new ArrayList<>();
    newPlayerPositions.add(newPosition);
    newPlayerPositions.add(newPosition2);

    Posn newPosition3 = new Posn(7, 6);
    Posn newPosition4 = new Posn(8, 8);
    List<Posn> newAdversaryPositions = new ArrayList<>();
    newAdversaryPositions.add(newPosition3);
    newAdversaryPositions.add(newPosition4);

    simpleGameState.intermediateGameState(newPlayerPositions, newAdversaryPositions, false);
  }

  @Test
  public void testIntermediateGameStateExitable() {
    GameStateModel simpleGameState = new GameStateModel(testUtils.createComplicatedLevel());

    Player player1 = new Player();
    Player player2 = new Player();

    player1.setPosition(new Posn(1, 1));
    player2.setPosition(new Posn(2, 2));

    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    List<Actor> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);

    List<Actor> adversaries = new ArrayList<>();
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    simpleGameState.initGameState(players, adversaries, new Posn(0, 0));

    assertFalse(simpleGameState.isExitable());

    Posn newPosition3 = new Posn(7, 6);
    Posn newPosition4 = new Posn(8, 8);
    List<Posn> newAdversaryPositions = new ArrayList<>();
    newAdversaryPositions.add(newPosition3);
    newAdversaryPositions.add(newPosition4);

    Posn newPosition = new Posn(1, 1);
    Posn newPosition2 = new Posn(2, 2);
    List<Posn> newPlayerPositions = new ArrayList<>();
    newPlayerPositions.add(newPosition);
    newPlayerPositions.add(newPosition2);


    simpleGameState.intermediateGameState(newPlayerPositions, newAdversaryPositions, true);

    assertTrue(simpleGameState.isExitable());
  }


  @Test
  public void testHandlePlayerExpulsion() {
    Level initLevel = testUtils.createComplicatedLevel();
    initLevel.initGrid();
    GameStateModel simpleGameState = new GameStateModel(initLevel);

    Player player1 = new Player();
    player1.setPosition(new Posn(0, 0));
    Actor player2 = new Player();

    List<Actor> actors = new ArrayList<>();
    actors.add(player1);
    actors.add(player2);

    simpleGameState.setActors(actors);

    assertEquals(new ArrayList<>(), simpleGameState.getExitedPlayers());
    simpleGameState.handlePlayerExpulsion(player1);

    List<Player> expelledPlayers = new ArrayList<>();
    expelledPlayers.add(player1);
    assertEquals(expelledPlayers, simpleGameState.getExitedPlayers());
  }

  @Test
  public void testHandleMovePlayer() {
    Level initLevel = testUtils.createComplicatedLevel();
    GameStateModel simpleGameState = new GameStateModel(initLevel);

    Player player1 = new Player();
    player1.setPosition(new Posn(0, 0));

    assertEquals(0, player1.getPosition().getRow());
    assertEquals(0, player1.getPosition().getCol());

    simpleGameState.handleMovePlayer(player1, new Posn(1, 0));

    assertEquals(1, player1.getPosition().getRow());
    assertEquals(0, player1.getPosition().getCol());
  }

}
