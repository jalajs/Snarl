import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.ExitKey;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Player;
import GameObjects.Adversary;
import User.LocalUser;

public class LocalUserTest {
  private final TestUtils testUtils = new TestUtils();

  /**
   * This tests the update method and ensures the correct fields
   * are modified (isExitable, surroundings, currentPositi
   *
   */
  @Test
  public void testUserUpdate() {
    LocalUser user = new LocalUser("Bob");
    List<List<Tile>> initSurroundings = new ArrayList<>();
    List<Tile> row1 = new ArrayList<>();
    List<Tile> row2 = new ArrayList<>();
    List<Tile> row3 = new ArrayList<>();

    Tile wallTile = new Tile(true);
    Tile traversableTile = new Tile(false);
    Tile playerTile = new Tile(false);
    Tile adversaryTile = new Tile(false);
    playerTile.setOccupier(new Player("Bob"));
    adversaryTile.setOccupier(new Adversary("Zombie","Jimbo"));

    row1.add(wallTile);
    row1.add(playerTile);
    row1.add(wallTile);

    row2.add(traversableTile);
    row2.add(traversableTile);
    row2.add(traversableTile);

    row3.add(wallTile);
    row3.add(traversableTile);
    row3.add(adversaryTile);

    initSurroundings.add(row1);
    initSurroundings.add(row2);
    initSurroundings.add(row3);

    user.setSurroundings(initSurroundings);
    user.setCurrentPosition(new Posn(0, 1));

    assertEquals(user.getSurroundings(), initSurroundings);
    assertFalse(user.isExitable());
    assertEquals(user.getCurrentPosition(),new Posn(0, 1) );

    List<List<Tile>> newSurroundings = new ArrayList<>();

    user.update(newSurroundings, true, new Posn(1, 2));

    assertEquals(user.getSurroundings(), newSurroundings);
    assertTrue(user.isExitable());
    assertEquals(user.getCurrentPosition(),new Posn(1, 2) );

  }

  /**
   * Tests that the helper 'visibileTileRepresentation' correctly renders a tile grid
   *
   */
  @Test
  public void testVisibleTileRepresentation() {
    List<List<Tile>> surroundings = new ArrayList<>();
    List<Tile> row1 = new ArrayList<>();
    List<Tile> row2 = new ArrayList<>();
    List<Tile> row3 = new ArrayList<>();

    Tile wallTile = new Tile(true);
    Tile traversableTile = new Tile(false);
    Tile playerTile = new Tile(false);
    Tile adversaryTile = new Tile(false);
    playerTile.setOccupier(new Player("Bob"));
    adversaryTile.setOccupier(new Adversary("Zombie","Jimbo"));

    row1.add(wallTile);
    row1.add(playerTile);
    row1.add(wallTile);

    row2.add(traversableTile);
    row2.add(traversableTile);
    row2.add(traversableTile);

    row3.add(wallTile);
    row3.add(traversableTile);
    row3.add(adversaryTile);

    surroundings.add(row1);
    surroundings.add(row2);
    surroundings.add(row3);

    LocalUser user = new LocalUser("Shirley");
    user.setSurroundings(surroundings);

    assertEquals( "XOX\n" +
            "...\n" +
            "X.#", user.visibleTileRepresentation());

  }

  /**
   * Tests method createPosn needed for the turn functionality
   */
  @Test
  public void testCreatePosn() {
    LocalUser user = new LocalUser("Bob");
    assertEquals(new Posn(1, 0), user.createPosn("1 0"));
    assertEquals(new Posn(0, 0), user.createPosn("0 0"));
    assertEquals(new Posn(40, 50), user.createPosn("40 50"));
  }

  /**
   * Even though this method is in Tile, it is only used in LocalUser currently
   */
  @Test
  public void testBuildAction(){
    Tile newTile = new Tile(false);
    newTile.setPosition(new Posn(1,1));

    assertEquals(newTile.buildAction(new Posn(1, 1), "Bob").getType(), "Do Nothing");

    Tile keyTile = new Tile(false);
    keyTile.setCollectable(new ExitKey(new Posn(1, 1)));

    assertEquals(keyTile.buildAction(new Posn(1, 1), "Bob").getType(), "Pick Up Key");

    Tile ejectTile = new Tile(false);
    Actor adversary = new Adversary("Zombie","Jimbo");
    ejectTile.setOccupier(adversary);

    assertEquals(ejectTile.buildAction(new Posn(0, 1), "Bob").getType(), "Eject");

    Tile moveTile = new Tile(false);
    moveTile.setPosition(new Posn(1, 1));

    assertEquals(moveTile.buildAction(new Posn(0, 1), "Bob").getType(), "Move");
  }


}
