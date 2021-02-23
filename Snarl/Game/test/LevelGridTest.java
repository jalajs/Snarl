import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class LevelGridTest {
    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testinitGrid() {

        Level emptyLevel = new Level(7, 7);
        assertEquals(emptyLevel.getRooms(), new ArrayList<>());
        assertEquals(emptyLevel.getHallways(), new ArrayList<>());


        Room room1 = testUtils.createComplicatedRoom();
        Room room2 = testUtils.createSimpleRoom();

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        List<Hallway> hallways = new ArrayList<>();
        Hallway hallway = testUtils.createHallway();
        hallways.add(hallway);

        emptyLevel.setRooms(rooms);
        emptyLevel.setHallways(hallways);

        // tile grid is still empty
        assertEquals(emptyLevel.getTileGrid(), new ArrayList<>());

    }


    @Test
    public void testSpawnActors() {
        Level level = testUtils.createComplicatedLevel();

        Map<Actor, Posn> actorMap = new HashMap<>();
        Actor player1 = new Player();
        Actor player2 = new Player();

        Actor adversary1 = new Adversary();
        Actor adversary2 = new Adversary();

        actorMap.put(player1, new Posn(0,0));
        actorMap.put(player2, new Posn(0, 1));
        actorMap.put(adversary1, new Posn(7, 7));
        actorMap.put(adversary2, new Posn(7, 8));

        assertEquals(level.spawnActors(2, 2), actorMap);
    }

    @Test
    public void testDropKey() {

    }
}
