import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class LevelTest {


  @Test
  public void testRoomToString() {
    // draw a simple un occuppied un walled room
    Room simpleRoom = new Room();
    List<ArrayList<Tile>> simpleTileGrid = new ArrayList<>();
    ArrayList<Tile> simpleTileRow = new ArrayList<>();
    simpleTileRow.add(new Tile(false));
    simpleTileRow.add(new Tile(false));

    simpleTileGrid.add(simpleTileRow);
    simpleTileGrid.add(simpleTileRow);

    simpleRoom.setTileGrid(simpleTileGrid);

    assertEquals(simpleRoom.toString(), "..\n..");

    // draw a room with walls
    Room walledRoom = new Room();
    List<ArrayList<Tile>> walledTileGrid = new ArrayList<>();
    ArrayList<Tile> wallTileRow = new ArrayList<>();
    wallTileRow.add(new Tile(true));
    wallTileRow.add(new Tile(true));
    wallTileRow.add(new Tile(true));

    walledTileGrid.add(wallTileRow);
    walledTileGrid.add(wallTileRow);
    walledTileGrid.add(wallTileRow);

    walledRoom.setTileGrid(walledTileGrid);

    assertEquals(walledRoom.toString(), "XXX\nXXX\nXXX");

    // draw a complicated room with doors and humanoids and collectables
    Room complicatedRoom = new Room();
    ExitKey key = new ExitKey();
    Humanoid humanoid = new Humanoid();
    Door door = new Door();
    Tile keyTile =  new Tile(false);
    Tile humanoidTile = new Tile(false);
    Tile doorTile = new Tile(false);
    humanoidTile.setOccupier(humanoid);
    keyTile.setCollectable(key);
    doorTile.setDoor(door);

    List<ArrayList<Tile>> complicatedTileGrid = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow1 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow2 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow3 = new ArrayList<>();
    ArrayList<Tile> complicatedTileRow4 = new ArrayList<>();
    complicatedTileRow1.add(keyTile);
    complicatedTileRow1.add(new Tile(false));
    complicatedTileRow1.add(new Tile(false));

    complicatedTileRow2.add(new Tile(true));
    complicatedTileRow2.add(new Tile(true));
    complicatedTileRow2.add(humanoidTile);

    complicatedTileRow3.add(doorTile);
    complicatedTileRow3.add(humanoidTile);
    complicatedTileRow3.add(new Tile(false));

    complicatedTileRow4.add(humanoidTile);
    complicatedTileRow4.add(new Tile(false));
    complicatedTileRow4.add(new Tile(true));

    complicatedTileGrid.add(complicatedTileRow1);
    complicatedTileGrid.add(complicatedTileRow2);
    complicatedTileGrid.add(complicatedTileRow3);
    complicatedTileGrid.add(complicatedTileRow4);


    complicatedRoom.setTileGrid(complicatedTileGrid);

    assertEquals(complicatedRoom.toString(), "K..\n" +
            "XX*\n" +
            "|*.\n" +
            "*.X");


  }

//  void testLevel() {
////    Tile occupiedWallTile = new Tile(true, true);
////    Tile unoccupiedWallTile = new Tile(false, true);
////    Tile occupiedTile = new Tile(true, false);
////    Tile unoccupiedTile = new Tile(false, false);
//    ArrayList<Tile> tilesR1 = new ArrayList<Tile>();
////    tilesR1.add(occupiedWallTile);
////    tilesR1.add(unoccupiedWallTile);
////    tilesR1.add(occupiedTile);
////    tilesR1.add(unoccupiedTile);
//    List<ArrayList<Tile>> tileGrid = new ArrayList<ArrayList<Tile>>();
//    tileGrid.add(tilesR1);
//
//    Room room1 = new Room(tilesR1, );
//
//  }


}
