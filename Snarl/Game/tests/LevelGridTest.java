import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds tests for methods pertaining to creation and mutation of the level grid
 */
public class LevelGridTest {
    private final TestUtils testUtils = new TestUtils();

    /**
     * This method tests the functionality of InitGrid, which adds the tiles
     *  in rooms and hallways to the level grid.
     */
    @Test
    public void testInitGrid() {
        Level level = testUtils.createComplicatedLevel();
        assertEquals(level.getRooms().size(), 4);
        assertEquals(level.getHallways().size(), 3);
        assertEquals(level.getLevelGrid()[0][0], " ");

        level.initGrid();

        assertEquals(level.getLevelGrid()[0][0], ".");
    }

    /**
     * The purpose of this test is to ensure spawnActors places actors on the grid
     * and gives them the proper positions
     */
    @Test
    public void testSpawnActors() {
        Level level = testUtils.createComplicatedLevel();
        level.initGrid();

        List<Actor> players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        players.add(player1);
        players.add(player2);

        List<Actor> adversaries = new ArrayList<>();
        Adversary adversary1 = new Adversary();
        Adversary adversary2 = new Adversary();
        adversaries.add(adversary1);
        adversaries.add(adversary2);

        level.spawnActors(players, adversaries);

        assertEquals(player1.getPosition().getX(), 0);
        assertEquals(player1.getPosition().getY(), 0);
        assertEquals(player2.getPosition().getX(), 0);
        assertEquals(player2.getPosition().getY(), 1);
        assertEquals(adversary1.getPosition().getX(), 13);
        assertEquals(adversary1.getPosition().getY(), 8);
        assertEquals(adversary2.getPosition().getX(), 13);
        assertEquals(adversary2.getPosition().getY(), 9);

        String[][] levelGrid = level.getLevelGrid();
        assertEquals(levelGrid[0][0], "O");
        assertEquals(levelGrid[0][1], "O");
        assertEquals(levelGrid[13][8], "#");
        assertEquals(levelGrid[13][9], "#");
    }

    /**
     * This method tests placing a list of actors that know their position inside the level
     */
    @Test
    public void testPlaceActorsInLevel() {
        Level level = testUtils.createComplicatedLevel();
        level.initGrid();

        assertEquals(level.getLevelGrid()[0][0], ".");
        assertEquals(level.getLevelGrid()[1][0], ".");
        assertEquals(level.getLevelGrid()[0][1], ".");


        Actor player1 = new Player();
        player1.setPosition(new Posn(0,0));
        Actor player2 = new Player();
        player2.setPosition(new Posn(0,1));
        Actor adversary = new Adversary();
        adversary.setPosition(new Posn(1,0));

        List<Actor> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(adversary);

        level.placeActorsInLevel(players);

        assertEquals(level.getLevelGrid()[0][0], "O");
        assertEquals(level.getLevelGrid()[1][0], "#");
        assertEquals(level.getLevelGrid()[0][1], "O");
    }

    /**
     * Tests that a key can be dropped in a level at a specified position. Also tests
     * that this key can be removed.
     *
     */
    @Test
    public void testDropAndRemoveKey() {
        Level level = testUtils.createComplicatedLevel();
        level.initGrid();
        level.dropKey(new Posn(0, 0));

        assertEquals(level.getLevelGrid()[0][0], "K");
        assertEquals(level.getExitKeyPosition().getY(), 0);
        assertEquals(level.getExitKeyPosition().getX(), 0);

        level.removeKey();

        assertEquals(level.getLevelGrid()[0][0], ".");
    }

    /**
     * This test ensures handle player move updates the new tile and the old tile
     * to reflect the players move. The player's posn should also be the new posn after
     * the move is handled
     */
    @Test
    public void testHandlePlayerMove() {
        Level level = testUtils.createComplicatedLevel();
        level.initGrid();

        Actor player = new Player();
        player.setPosition(new Posn(0,0));
        List<Actor> players = new ArrayList<>();
        players.add(player);
        level.placeActorsInLevel(players);

        assertEquals(level.getLevelGrid()[0][0], "O");

        level.handlePlayerMove(player, new Posn(0, 1));

        assertEquals(level.getLevelGrid()[0][0], ".");
        assertEquals(level.getLevelGrid()[0][1], "O");
    }

    /**
     * This method tests expelling a player from a level. The player's tile should be unocuppied
     * after expulsion.
     */
    @Test
    public void testExpelPlayer() {
        Level level = testUtils.createComplicatedLevel();
        level.initGrid();

        Actor player = new Player();
        player.setPosition(new Posn(0,0));
        List<Actor> players = new ArrayList<>();
        players.add(player);
        level.placeActorsInLevel(players);

        assertEquals(level.getLevelGrid()[0][0], "O");

        level.expelPlayer(player);

        assertEquals(level.getLevelGrid()[0][0], ".");
    }
}
