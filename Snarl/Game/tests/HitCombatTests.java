import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import Action.InteractionType;
import Action.MoveAction;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.AdversaryType;
import GameObjects.Player;
import GameObjects.Level;
import GameObjects.Posn;

import GameState.GameState;
import GameState.GameStateModel;
import User.LocalUser;
import User.User;

import static org.junit.Assert.assertEquals;

/**
 * Tests for hit combat, added functionality
 */
public class HitCombatTests {
  private final TestUtils testUtils = new TestUtils();


  /**
   * Tests to see that player/user loses the appropriate hit points when faced with damage
   */
  @Test
  public void testPlayerDamage() {
    Player playerOne = new Player();
    assertEquals(100, playerOne.getHitPoints());
    playerOne.subtractFromHitPoints(25);
    assertEquals(75, playerOne.getHitPoints());

    User user1 = new LocalUser();
    assertEquals(100, user1.getHitPoints());
    user1.subtractFromHitPoints(50);
    assertEquals(50, user1.getHitPoints());
  }

  /**
   * This test ensures that the gameState method 'calculateInteractionType' produces the expected
   * interaction type
   */
  @Test
  public void testCalculateInteractionType() {
    Level level = testUtils.createComplicatedLevel();
    level.createLevelExit(new Posn(1, 1));
    GameState gameState = new GameStateModel(level);
    Player player = new Player("Jimbo");
    player.setPosition(new Posn(0, 0));
    Adversary adversary = new Adversary(AdversaryType.ZOMBIE, "Zombie1");
    adversary.setPosition(new Posn(2, 0));
    List<Actor> players = new ArrayList<>();
    players.add(player);
    List<Actor> adversaries = new ArrayList<>();
    adversaries.add(adversary);
    gameState.initGameStateWhereActorsHavePositions(players, adversaries, new Posn(1, 0));

    // check interaction types from user movements
    InteractionType interactionTypeOK = gameState.calculateInteractionType(player, new Posn(0, 1));
    assertEquals(InteractionType.OK, interactionTypeOK);

    InteractionType interactionTypeKEY = gameState.calculateInteractionType(player, new Posn(1, 0));
    assertEquals(InteractionType.KEY, interactionTypeKEY);

    InteractionType interactionTypeATTACK = gameState.calculateInteractionType(player, new Posn(2, 0));
    assertEquals(InteractionType.ATTACK, interactionTypeATTACK);

    InteractionType interactionTypeEXIT = gameState.calculateInteractionType(player, new Posn(1, 1));
    assertEquals(InteractionType.EXIT, interactionTypeEXIT);
  }

  /**
   * This method tests the handling of the interaction type produced by the adversary
   */
  @Test
  public void handleAdversaryInteractionType() {
    Level level = testUtils.createComplicatedLevel();
    level.setExitDoorPosition(new Posn(1, 1));
    GameState gameState = new GameStateModel(level);
    Player player = new Player("Jimbo");
    player.setPosition(new Posn(0, 0));
    Adversary adversary = new Adversary(AdversaryType.ZOMBIE, "Zombie1");
    adversary.setPosition(new Posn(2, 0));
    List<Actor> players = new ArrayList<>();
    players.add(player);
    List<Actor> adversaries = new ArrayList<>();
    gameState.initGameStateWhereActorsHavePositions(players, adversaries, new Posn(1, 0));

    // test an OK move
    MoveAction simpleMoveAction = new MoveAction(new Posn(0, 1), adversary.getPosition(), 25);
    simpleMoveAction.setInteractionType(InteractionType.OK);
    gameState.handleInteractionType(adversary, simpleMoveAction);
    assertEquals(new Posn(0, 1), adversary.getPosition());

    // test an ATTACK move
    MoveAction attackMoveAction = new MoveAction(new Posn(0, 0), adversary.getPosition(), 25);
    attackMoveAction.setInteractionType(InteractionType.ATTACK);
    gameState.handleInteractionType(adversary, attackMoveAction);
    assertEquals(player.getHitPoints(), 75);
    assertEquals(player.getName(), attackMoveAction.getVictimName());

    // test an EJECT move
    MoveAction ejectMoveAction = new MoveAction(new Posn(0, 0), adversary.getPosition(), 25);
    ejectMoveAction.setInteractionType(InteractionType.EJECT);
    gameState.handleInteractionType(adversary, ejectMoveAction);
    assertEquals(new Posn(0, 0), adversary.getPosition());
    assertEquals(1, gameState.getEjectedPlayers().size());
  }

  @Test
  public void handlePlayerInteractionType() {
    Level level = testUtils.createComplicatedLevel();
    level.setExitDoorPosition(new Posn(1, 1));
    GameState gameState = new GameStateModel(level);
    Player player = new Player("Jimbo");
    player.setPosition(new Posn(0, 0));
    Adversary adversary = new Adversary(AdversaryType.ZOMBIE, "Zombie1");
    adversary.setPosition(new Posn(2, 0));
    Adversary adversary2 = new Adversary(AdversaryType.ZOMBIE, "Zombie2");
    adversary2.setPosition(new Posn(2, 1));
    List<Actor> players = new ArrayList<>();
    players.add(player);
    List<Actor> adversaries = new ArrayList<>();
    adversaries.add(adversary);
    adversaries.add(adversary2);
    gameState.initGameStateWhereActorsHavePositions(players, adversaries, new Posn(1, 0));

    MoveAction simpleMoveAction = new MoveAction( new Posn(0, 1), player.getPosition());
    simpleMoveAction.setInteractionType(InteractionType.OK);
    gameState.handleInteractionType(player, simpleMoveAction);
    // player moved (game state did not change)
    assertEquals(new Posn(0, 1), player.getPosition());


    MoveAction attackMoveAction = new MoveAction(new Posn(2, 0), player.getPosition());
    attackMoveAction.setInteractionType(InteractionType.ATTACK);

    assertEquals(100, player.getHitPoints());
    assertEquals("", attackMoveAction.getVictimName());
    assertEquals(0, attackMoveAction.getDamage());
    gameState.handleInteractionType(player, attackMoveAction);
    // player suffered damage, and the action now has a victim name and damage associated with it
    assertEquals(75, player.getHitPoints());
    assertEquals("Jimbo", attackMoveAction.getVictimName());
    assertEquals(25, attackMoveAction.getDamage());
    assertEquals(InteractionType.ATTACK, attackMoveAction.getInteractionType());
    
    // set player to 25 hitpoints
    player.setHitPoints(25);

    assertEquals(0, gameState.getEjectedPlayers().size());
    // attacking/hurting themself now should eject them
    MoveAction attackMoveAction2 = new MoveAction(new Posn(2, 1), player.getPosition());
    attackMoveAction2.setInteractionType(InteractionType.ATTACK);
    gameState.handleInteractionType(player, attackMoveAction2);
    assertEquals(0, player.getHitPoints());
    assertEquals("Jimbo", attackMoveAction2.getVictimName());
    assertEquals(25, attackMoveAction2.getDamage());
    assertEquals(InteractionType.EJECT, attackMoveAction2.getInteractionType());
    assertEquals(1, gameState.getEjectedPlayers().size());

  }
}
