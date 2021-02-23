import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GameStateTests {
    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testInitGameState() {
        GameStateModel simpleGameState = new GameStateModel(testUtils.createComplicatedLevel());
        Level initLevel = simpleGameState.getLevel();

        assertEquals(simpleGameState.getActorPositions(), new HashMap<Actor, Posn>());
        assertEquals(simpleGameState.getObjectPositions(), new HashMap<Collectable, Posn>());
        assertFalse(initLevel.createLevelString().contains("K"));

        simpleGameState.initGameState(2, 2, new Posn(0, 9));


        Level mutatedLevel = simpleGameState.getLevel();
        assertEquals(simpleGameState.getActorPositions().size(), 4);
        assertEquals(initLevel.createLevelString(), "OO.    ...\n" +
                "..X    ..K\n" +
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

}
