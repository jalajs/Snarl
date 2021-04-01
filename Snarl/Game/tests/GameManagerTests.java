import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import GameManager.GameManagerClass;
import GameObjects.Actor;
import Action.MoveAction;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameState.GameStateModel;


import static org.junit.Assert.*;


public class GameManagerTests {
  private final TestUtils testUtils = new TestUtils();

  @Test
  public void testAddPlayer() {
    GameManagerClass gameManager = new GameManagerClass();
    assertEquals(gameManager.getUsers().size(), 0);
    gameManager.addPlayer("Billy bob");
    assertEquals(gameManager.getUsers().size(), 1);
    assertEquals(gameManager.getUsers().get(0).getName(), "Billy bob");
  }

  @Test
  public void testAddAdversaries() {
    GameManagerClass gameManager = new GameManagerClass();
    assertEquals(gameManager.getAdversaries().size(), 0);
    gameManager.addAdversary("AI", "Jo");
    assertEquals(gameManager.getAdversaries().size(), 1);
    assertEquals("AI", gameManager.getAdversaries().get(0).getType());
  }

  /**
   * This test verfies updateTurn increases the turn count
   * on every call, and sets the turn count to 0 at the end of each round
   *
   */
  @Test
  public void testUpdateTurn() {
    GameManagerClass gameManager = new GameManagerClass();
    gameManager.addPlayer("Billy bob");
    gameManager.addPlayer("John");
    gameManager.addPlayer("Nick");

    assertEquals(0, gameManager.getTurn());
    gameManager.updateTurn();
    assertEquals(1, gameManager.getTurn());
    gameManager.updateTurn();
    assertEquals(2, gameManager.getTurn());
    gameManager.updateTurn();
    assertEquals(0, gameManager.getTurn());
  }

  /**
   * Tests that when provided with a moveaction, the manager is able to send the moveaction
   * to the gameState. In the gameState, the move and resulting interaction must be validated and
   * completed.
   *
   * This test is mainly concerned that moveaction produces the correct results. See the gameState tests
   * for specific move/interaction tests.
   *
   */
  @Test
  public void testExecuteAction() {
    Level level = testUtils.createComplicatedLevel();
    GameStateModel gs = new GameStateModel(level);
    GameManagerClass manager = new GameManagerClass();
    manager.addPlayer("Jalaj");
    manager.addPlayer("Megan");
    manager.addAdversary("Zombie", "Rolph");
    manager.addAdversary("Zombie", "Mr Bean");
    manager.setTurn(0);
    manager.setGs(gs);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player("Jalaj");
    Player player2 = new Player("Megan");
    Adversary adversary1 = new Adversary("Zombie", "Carl");
    Adversary adversary2 = new Adversary("Zombie", "Robert");

    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    gs.initGameState(players, adversaries, new Posn(2, 0));

    // this is a valid position, the player should move here
    Posn destination1 = new Posn(2, 0);
    MoveAction move1 = new MoveAction(destination1, player1.getPosition());
    assertEquals(player1.getPosition(), new Posn(0, 0));
    assertEquals(gs.getLevel().getExitKeyPosition(), new Posn(2, 0));
    assertFalse(gs.isExitable());
//    manager.executeAction("player", move1);
    assertEquals(player1.getPosition(), destination1);
    assertTrue(gs.isExitable());

    // this is outside the level, should not be allowed
    Posn destination2 = new Posn( 10, 10);
    MoveAction move2 = new MoveAction(destination2, player1.getPosition());
  //  manager.executeAction("player", move2);
    //assertEquals(player1.getPosition(), destination1); // player does not move if an invalid action is made from the user

    // this is a wall, player should not move here
    Posn destination3 = new Posn( 1, 2);
    MoveAction move3 = new MoveAction(destination3, player1.getPosition());
  //  manager.executeAction("player", move3);
    // assertEquals(player1.getPosition(), destination1);

    // this another player, player should not move here
    Posn destination4 = new Posn(0, 1);
    MoveAction move4 = new MoveAction(destination4, player1.getPosition());
    //manager.executeAction("player", move4);
    //assertEquals(player1.getPosition(), destination1);


  }
}
