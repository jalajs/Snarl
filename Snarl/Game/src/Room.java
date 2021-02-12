import java.util.ArrayList;
import java.util.List;

public class Room {
  private List<ArrayList<Tile>> tileGrid;
  private Posn upperLeft;
  private int xDim;
  private int yDim;
  private List<Collectables> collectables;
  private List<Door> doors;


  public Room() {
    this.tileGrid = new ArrayList<ArrayList<Tile>>();
    this.upperLeft = new Posn(0, 0);
    this.xDim = 0;
    this.yDim = 0;
    this.collectables = new ArrayList<>();
    this.doors = new ArrayList<>();
  }

  public Room(List<ArrayList<Tile>> tileGrid, Posn upperLeft, int xDim, int yDim, List<Collectables> collectables, List<Door> doors) {
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

  public List<Collectables> getCollectables() {
    return collectables;
  }

  public void setCollectables(List<Collectables> collectables) {
    this.collectables = collectables;
  }

  public List<Door> getDoorPositions() {
    return this.doors;
  }

  public void setDoorPositions(List<Door> doorPositions) {
    this.doors = doors;
  }
}
