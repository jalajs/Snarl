import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LevelTest {


  @Test
  void testDrawLevel() {
    Tile occupiedWallTile = new Tile(true, true);
    Tile unoccupiedWallTile = new Tile(false, true);
    Tile occupiedTile = new Tile(true, false);
    Tile unoccupiedTile = new Tile(false, false);
    ArrayList<Tile> tilesR1 = new ArrayList<Tile>();
    tilesR1.add(occupiedWallTile);
    tilesR1.add(unoccupiedWallTile);
    tilesR1.add(occupiedTile);
    tilesR1.add(unoccupiedTile);
    List<ArrayList<Tile>> tileGrid = new ArrayList<ArrayList<Tile>>();
    tileGrid.add(tilesR1);

    Room room1 = new Room(tilesR1, );

  }


}
