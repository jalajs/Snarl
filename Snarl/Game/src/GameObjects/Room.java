package GameObjects;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Collectable;
import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Tile;

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

  /**
   * Determines whether or not the given posn is located in this room.
   * @param posn the given position
   * @return boolean - whether or not the given posn is in the room
   *
   * example room and posn that should return negative
   *  ---------------------
   *          +
   *              xxxxx
   *              xxxxx
   *
   *  ---------------------
   */
  public boolean isPosnInRoom(Posn posn) {
    int givenX = posn.getX();
    int givenY = posn.getY();

    // create ranges since i an j are coordinates from the entire level/world
    int xRange = this.upperLeft.getX() + this.xDim;
    int yRange = this.upperLeft.getY() + this.yDim;

    return givenX < xRange && givenX >= this.upperLeft.getX()
            && givenY < yRange && givenY >= this.upperLeft.getY()
            && givenX > -1 && givenY > -1;
    }


  /**
   * Return the positions of unoccupied tiles acessible via one cardinal move from the given player
   * position.
   *            cardinal moves are up, down, right, or left
   *                            []
   *                          []P[]
   *                           []
   *
   * @param playerPosn the position of the player relative to the level
   * @return the list of positions of accessible tiles relative to the level
   *
   */
    public List<Posn> getNextPossibleCardinalMoves(Posn playerPosn) {
      List<Posn> possiblePosns = new ArrayList<>();

      int xRange = this.upperLeft.getX() + this.xDim;
      int yRange = this.upperLeft.getY() + this.yDim;

      int playerX  = playerPosn.getX();
      int playerY = playerPosn.getY();

      // find the north tile
      if ((playerX - 1) - this.upperLeft.getX() >= 0) {
        int roomX = (playerX - 1) - this.upperLeft.getX();
        int roomY = playerY - this.upperLeft.getY();
        Tile north = this.tileGrid.get(roomX).get(roomY);
        int x = playerX - 1;
        if (!north.getisWall()) {
          possiblePosns.add(new Posn(playerX - 1,playerY));
        }
      }
      // find the west tile
      if ((playerY - 1) - this.upperLeft.getY() >= 0) {
        int roomX = playerX - this.upperLeft.getX();
        int roomY = (playerY - 1) - this.upperLeft.getY();
        Tile west = this.tileGrid.get(roomX).get(roomY);
        if (!west.getisWall()) {
          possiblePosns.add(new Posn(playerX,playerY - 1));
        }
      }
      // find the east tile
      if ((playerY + 1) < yRange) {
        int roomX = playerX - this.upperLeft.getX();
        int roomY = (playerY + 1) - this.upperLeft.getY();
        Tile east = this.tileGrid.get(roomX).get(roomY);
        if (!east.getisWall()) {
          possiblePosns.add(new Posn(playerX,playerY + 1));
        }
      }
      // find the south tile
      if ((playerX + 1)  < xRange) {
        int roomX = (playerX + 1) - this.upperLeft.getX();
        int roomY = playerY - this.upperLeft.getY();
        Tile south = this.tileGrid.get(roomX).get(roomY);
        if (!south.getisWall()) {
          possiblePosns.add(new Posn(playerX + 1,playerY));
        }
      }


      return possiblePosns;
    }
}
