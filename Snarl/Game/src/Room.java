import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room in the level dungeon. Each room has a tile grid, upper-left Cartesian position,
 * dimensions, a list of collectables, and one or more doors.
 */
public class Room {
  private List<ArrayList<Tile>> tileGrid;
  private Posn upperLeft;
  private int xDim;
  private int yDim;
  private List<Collectable> collectables;
  private List<Door> doors;


  public Room() {
    this.tileGrid = new ArrayList<>();
    this.upperLeft = new Posn(0, 0);
    this.xDim = 0;
    this.yDim = 0;
    this.collectables = new ArrayList<>();
    this.doors = new ArrayList<>();
  }

  public Room(List<ArrayList<Tile>> tileGrid, Posn upperLeft, int xDim, int yDim, List<Collectable> collectables, List<Door> doors) {
    this.tileGrid = tileGrid;
    this.upperLeft = upperLeft;
    this.xDim = xDim;
    this.yDim = yDim;
    this.collectables = collectables;
    this.doors = doors;
  }

  public List<ArrayList<Tile>> getTileGrid() {
    return tileGrid;
  }

  public void setTileGrid(List<ArrayList<Tile>> tileGrid) {
    this.tileGrid = tileGrid;
  }

  public Posn getUpperLeft() {
    return upperLeft;
  }

  public void setUpperLeft(Posn upperLeft) {
    this.upperLeft = upperLeft;
  }

  public int getxDim() {
    return xDim;
  }

  public void setxDim(int xDim) {
    this.xDim = xDim;
  }

  public int getyDim() {
    return yDim;
  }

  public void setyDim(int yDim) {
    this.yDim = yDim;
  }

  public List<Collectable> getCollectables() {
    return collectables;
  }

  public void setCollectables(List<Collectable> collectables) {
    this.collectables = collectables;
  }

  public List<Door> getDoorPositions() {
    return this.doors;
  }

  public void setDoorPositions(List<Door> doorPositions) {
    this.doors = doorPositions;
  }


  /**
   * toString for an individual room.
   * @return One string rendering the visual contents of this room
   */
  @Override
  public String toString() {
    String roomAcc = "";
    for (int i = 0; i < tileGrid.size(); i ++) {
      for (int j = 0; j < tileGrid.get(i).size(); j ++) {
        roomAcc += tileGrid.get(i).get(j).toString();
      }
      if (i != tileGrid.size() - 1) {
        roomAcc += "\n";
      }
    }
    return roomAcc;
  }

  /**
   * Renders a room and its contents.
   * @return A 2D list for all of the String representations of the tiles in the room
   */
  public List<ArrayList<String>> renderRoom() {
    List<ArrayList<String>> roomAcc = new ArrayList<ArrayList<String>>();
    for (int i = 0; i < tileGrid.size(); i ++) {
      ArrayList<String> rowAcc = new ArrayList<>();
      for (int j = 0; j < tileGrid.get(i).size(); j ++) {
        rowAcc.add(tileGrid.get(i).get(j).toString());
      }
     roomAcc.add(rowAcc);
    }
    return roomAcc;
  }

}
