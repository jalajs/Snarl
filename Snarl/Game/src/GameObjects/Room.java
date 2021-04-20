package GameObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a room in the level dungeon. Each room has a tile grid, upper-left Cartesian position,
 * dimensions, a list of collectables, and one or more doors.
 */
public class Room {
  private Tile[][] tileGrid;
  private Posn upperLeft;
  private int rows;
  private int cols;
  private List<Collectable> collectables;
  private List<Door> doors;
  private Random rand;


  public Room() {
    this.tileGrid = new Tile[rows][cols];
    this.upperLeft = new Posn(0, 0);
    this.rows = 0;
    this.cols = 0;
    this.collectables = new ArrayList<>();
    this.doors = new ArrayList<>();
    this.rand = new Random();
  }

  public Room(Tile[][] tileGrid, Posn upperLeft, int rows, int cols, List<Collectable> collectables, List<Door> doors) {
    this.tileGrid = tileGrid;
    this.upperLeft = upperLeft;
    this.rows = rows;
    this.cols = cols;
    this.collectables = collectables;
    this.doors = doors;
    this.rand = new Random();
  }


  public Posn getUpperLeft() {
    return upperLeft;
  }

  public void setUpperLeft(Posn upperLeft) {
    this.upperLeft = upperLeft;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getCols() {
    return cols;
  }

  public void setCols(int cols) {
    this.cols = cols;
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
    String[][] roomAcc = new String[rows][cols];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        roomAcc[row][col] = tileGrid[row][col].toString();
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
    int row = posn.getRow();
    int col = posn.getCol();

    // create ranges since i an j are coordinates from the entire level/world
    int rowRange = this.upperLeft.getRow() + this.rows;
    int colRange = this.upperLeft.getCol() + this.cols;

    return row < rowRange && row >= this.upperLeft.getRow()
            && col < colRange && col >= this.upperLeft.getCol()
            && row > -1 && col > -1;
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

    int rowRange = this.upperLeft.getRow() + this.rows;
    int colRange = this.upperLeft.getCol() + this.cols;

    int playerRow = playerPosn.getRow();
    int playerCol = playerPosn.getCol();

    // find the north tile
    if ((playerRow - 1) - this.upperLeft.getRow() >= 0) {
      int roomRow = (playerRow - 1) - this.upperLeft.getRow();
      int roomCol = playerCol - this.upperLeft.getCol();
      Tile north = this.tileGrid[roomRow][roomCol];
      int row = playerRow - 1;
      if (!north.isWall()) {
        possiblePosns.add(new Posn(playerRow - 1, playerCol));
      }
    }
    // find the west tile
    if ((playerCol - 1) - this.upperLeft.getCol() >= 0) {
      int roomRow = playerRow - this.upperLeft.getRow();
      int roomCol = (playerCol - 1) - this.upperLeft.getCol();
      Tile west = this.tileGrid[roomRow][roomCol];
      if (!west.isWall()) {
        possiblePosns.add(new Posn(playerRow, playerCol - 1));
      }
    }
    // find the east tile
    if ((playerCol + 1) < colRange) {
      int roomRow = playerRow - this.upperLeft.getRow();
      int roomCol = (playerCol + 1) - this.upperLeft.getCol();
      Tile east = this.tileGrid[roomRow][roomCol];
      if (!east.isWall()) {
        possiblePosns.add(new Posn(playerRow, playerCol + 1));
      }
    }
    // find the south tile
    if ((playerRow + 1) < rowRange) {
      int roomRow = (playerRow + 1) - this.upperLeft.getRow();
      int roomCol = playerCol - this.upperLeft.getCol();
      Tile south = this.tileGrid[roomRow][roomCol];
      if (!south.isWall()) {
        possiblePosns.add(new Posn(playerRow + 1, playerCol));
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
   * @returna A posn which has had this rooms origin's row and col added to its row and col values
   */
  public Posn posnRelativeToLevel(Posn posn) {
    int originRow = this.upperLeft.getRow();
    int originCol = this.upperLeft.getCol();
    int row = posn.getRow();
    int col = posn.getCol();
    return new Posn(row + originRow, col + originCol);
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

  /**
   * Checks that there is a tile in the level with no wall or adversary. This means that a
   * ghost can appear in the room
   *
   * @return true is a valid tile exists within the room
   */
  public boolean checkForValidSpace() {
    for (int i = 0; i < this.tileGrid.length; i++) {
      for (int j = 0; j < this.tileGrid[i].length; j++) {
        Tile tile = this.tileGrid[i][j];
        // return true if there is a tile that is not a wall and is occupied by a player
        if (tile.validTransportTile()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get a random valid space in this room for the ghost to spawn to
   * @return the position of the random valid space
   */
  public Posn getRandomValidSpace() {
    int row = rand.nextInt(rows);
    int col = rand.nextInt(cols);
    Tile tile = this.tileGrid[row][col];
    while (!tile.validTransportTile()) {
      row = rand.nextInt(rows);
      col = rand.nextInt(cols);
      tile = this.tileGrid[row][col];
    }
    return new Posn(row + upperLeft.getRow(), col + upperLeft.getCol());
  }

  /**
   * Helper method to generates the position for a random unoccupied tile in this room.
   *
   * @return Posn of random unoccupied tile relative to the level
   */
  public Posn generateRandomUnoccupiedTile() {
    Posn posn = new Posn(-1, -1);
    Random random = new Random();
    int counter = 0;
    while (counter < rows * cols) {
      int randomX = random.nextInt(rows);
      int randomY = random.nextInt(cols);
      String tileString = tileGrid[randomX][randomY].toString();
      if (tileString.equals(".") || tileString.equals("K")) {
        posn.setRow(randomX);
        posn.setCol(randomY);
        break;
      }
      counter++;
    }
    return posnRelativeToLevel(posn);
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

  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }
}
