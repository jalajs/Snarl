package GameObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import GameObjects.Collectable;
import GameObjects.Door;
import GameObjects.Posn;
import GameObjects.Tile;

/**
 * Represents a room in the level dungeon. Each room has a tile grid, upper-left Cartesian position,
 * dimensions, a list of collectables, and one or more doors.
 */
public class Room {
  private Tile[][] tileGrid;
  private Posn upperLeft;
  private int xDim;
  private int yDim;
  private List<Collectable> collectables;
  private List<Door> doors;


  public Room() {
    this.tileGrid = new Tile[xDim][yDim];
    this.upperLeft = new Posn(0, 0);
    this.xDim = 0;
    this.yDim = 0;
    this.collectables = new ArrayList<>();
    this.doors = new ArrayList<>();
  }

  public Room(Tile[][] tileGrid, Posn upperLeft, int xDim, int yDim, List<Collectable> collectables, List<Door> doors) {
    this.tileGrid = tileGrid;
    this.upperLeft = upperLeft;
    this.xDim = xDim;
    this.yDim = yDim;
    this.collectables = collectables;
    this.doors = doors;
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
   *
   * @return One string rendering the visual contents of this room
   */
  @Override
  public String toString() {
    String roomAcc = "";
    for (int i = 0; i < tileGrid.length; i++) {
      for (int j = 0; j < tileGrid[i].length; j++) {
        roomAcc += tileGrid[i][j].toString();
      }
      if (i != tileGrid.length - 1) {
        roomAcc += "\n";
      }
    }
    return roomAcc;
  }

  /**
   * Renders a room and its contents.
   *
   * @return A 2D list for all of the String representations of the tiles in the room
   */
  public String[][] renderRoom() {
    String[][] roomAcc = new String[xDim][yDim];
    for (int i = 0; i < xDim; i++) {
      for (int j = 0; j < yDim; j++) {
        roomAcc[i][j] = tileGrid[i][j].toString();
      }
    }
    return roomAcc;
  }

  /**
   * Determines whether or not the given posn is located in this room.
   *
   * @param posn the given position
   * @return boolean - whether or not the given posn is in the room
   * <p>
   * example room and posn that should return negative --------------------- + xxxxx xxxxx
   * <p>
   * ---------------------
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
   * position. cardinal moves are up, down, right, or left [] []P[] []
   *
   * @param playerPosn the position of the player relative to the level
   * @return the list of positions of accessible tiles relative to the level
   */
  public List<Posn> getNextPossibleCardinalMoves(Posn playerPosn) {
    List<Posn> possiblePosns = new ArrayList<>();

    int xRange = this.upperLeft.getX() + this.xDim;
    int yRange = this.upperLeft.getY() + this.yDim;

    int playerX = playerPosn.getX();
    int playerY = playerPosn.getY();

    // find the north tile
    if ((playerX - 1) - this.upperLeft.getX() >= 0) {
      int roomX = (playerX - 1) - this.upperLeft.getX();
      int roomY = playerY - this.upperLeft.getY();
      Tile north = this.tileGrid[roomX][roomY];
      int x = playerX - 1;
      if (!north.getisWall()) {
        possiblePosns.add(new Posn(playerX - 1, playerY));
      }
    }
    // find the west tile
    if ((playerY - 1) - this.upperLeft.getY() >= 0) {
      int roomX = playerX - this.upperLeft.getX();
      int roomY = (playerY - 1) - this.upperLeft.getY();
      Tile west = this.tileGrid[roomX][roomY];
      if (!west.getisWall()) {
        possiblePosns.add(new Posn(playerX, playerY - 1));
      }
    }
    // find the east tile
    if ((playerY + 1) < yRange) {
      int roomX = playerX - this.upperLeft.getX();
      int roomY = (playerY + 1) - this.upperLeft.getY();
      Tile east = this.tileGrid[roomX][roomY];
      if (!east.getisWall()) {
        possiblePosns.add(new Posn(playerX, playerY + 1));
      }
    }
    // find the south tile
    if ((playerX + 1) < xRange) {
      int roomX = (playerX + 1) - this.upperLeft.getX();
      int roomY = playerY - this.upperLeft.getY();
      Tile south = this.tileGrid[roomX][roomY];
      if (!south.getisWall()) {
        possiblePosns.add(new Posn(playerX + 1, playerY));
      }
    }


    return possiblePosns;
  }

  /**
   * Returns all of the room origins that are immediately reachable from this room
   *
   * @return list of room origins
   */
  public List<Posn> checkReachableRooms() {
    List<Posn> positions = new ArrayList<>();
    for (Door d : doors) {
      Posn doorPosnRelToLevel = posnRelativeToLevel(d.getTileCoord());
      if (!d.isLevelExit()) {
        Hallway hallway = d.getHallway();
        List<Door> doors = hallway.getDoors();
        if (doors.get(0).getTileCoord().equals(doorPosnRelToLevel)) {
          positions.add(doors.get(1).getRoom().getUpperLeft());
        } else if (doors.get(1).getTileCoord().equals(doorPosnRelToLevel)) {
          positions.add(doors.get(0).getRoom().getUpperLeft());
        }
      }
    }
    return positions;
  }

  /**
   * Add the given door to this room's list of doors field
   */
  public void addDoor(Door door) {
    this.doors.add(door);
  }

  /**
   * updates the given posn such that its coordinates are relative to the entire level.
   * @param posn the given posn
   * @returna A posn which has had this rooms origin's X and Y added to its X and Y values
   */
  public Posn posnRelativeToLevel(Posn posn) {
    int originX = this.upperLeft.getX();
    int originY = this.upperLeft.getY();
    int x = posn.getX();
    int y = posn.getY();
    return new Posn(x + originX, y + originY);
  }



  /**
   * Connects this rooms's doors to the given hallways doors field.
   * @param hallways The given list of hallways
   */
  public void connectDoorsToHallways(List<Hallway> hallways) {
    for (Door door : this.doors) {
      Posn doorPosnRelToLevel = posnRelativeToLevel(door.getTileCoord());
      for (Hallway hallway : hallways) {
        List<Door> hallwayDoors = hallway.getDoors();
        if (hallwayDoors.get(0).getTileCoord().equals(doorPosnRelToLevel)
                || hallwayDoors.get(1).getTileCoord().equals(doorPosnRelToLevel)) {
          door.setHallway(hallway);
        }
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Room room = (Room) o;
    return upperLeft.equals(room.upperLeft);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upperLeft);
  }

  public Tile[][] getTileGrid() {
    return tileGrid;
  }

  public void setTileGrid(Tile[][] tileGrid) {
    this.tileGrid = tileGrid;
  }

  public List<Door> getDoors() {
    return doors;
  }

  public void setDoors(List<Door> doors) {
    this.doors = doors;
  }
}
