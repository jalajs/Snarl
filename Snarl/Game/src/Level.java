import java.util.ArrayList;
import java.util.List;

/**
 *  represents a game level, including all rooms, hallways, dimensions, and the grid
 *  an example level could look like this:
 *   ...    ...
 *   ..X    ...               this level has 4 rooms and 3 hallways
 *   ..|....|.|
 *            .               KEY
 *            .               . = unoccuppied tile
 *            .               X = wall
 *   ...|.....+               + = waypoint
 *   XX.X                     | = door
 *   ....                     empty space = no tiles
 *   ...|
 *   .
 *   .
 *   .
 *   +...|..
 *       ..|
 *
 *   This representation is produced and tested in .../test/LevelRepresentationTest.java
*/
public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;
  private int levelX;
  private int levelY;
  private String[][] levelGrid;

  /**
  * this basic no input constructor creates an empty 10x10 level (mostly used for testing)
  */
  public Level() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelX = 10;
    this.levelY = 10;
    this.levelGrid = new String[levelX][levelY];
    this.initGridSpace();
  }

  /**
   * this constructor allows for manual setting of x & y dimensions
   * @param levelX the x dimensions for the level
   * @param levelY the y dimensions for the level
   */
  public Level(int levelX, int levelY) {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelX = levelX;
    this.levelY = levelY;
    this.levelGrid = new String[this.levelX][this.levelY];
    this.initGridSpace();
  }


  /**
   * populates the entire grid of the level with an initial value of "  "
   */
  private void initGridSpace() {
    for (int i = 0; i < this.levelX; i++) {
      for (int j = 0; j < this.levelY; j++) {
        this.levelGrid[i][j] = " ";
      }
    }
  }

  /**
   * populate our level grid with rooms (and hallways)
   */
  private void initLevelGrid() {
    this.addRooms();
    this.addHallways();
  }

  /**
   * populates the levelGrid with hallways using the List<Hallway> in this level
   */
  private void addHallways() {
    for (Hallway hallway : this.hallways) {
      List<Posn> waypoints = hallway.getWaypoints();
      List<ArrayList<Tile>> segments = hallway.getTileSegments();
      List<Door> doors = hallway.getDoors();
      // populate waypoints
      addWayPoints(waypoints);
      // add door to list of waypoints
      List<Posn> allPoints = waypoints;
      allPoints.add(0, doors.get(0).getTileCoord());
      allPoints.add(doors.get(1).getTileCoord());
      // add tiles between waypoints
      addSegements(segments, allPoints);

    }
  }

  /**
   * place waypoints in level grid
   * @param waypoints the positions of the waypoints to be placed in the grid
   */
  private void addWayPoints(List<Posn> waypoints) {
    for (Posn posn : waypoints) {
      this.levelGrid[posn.getX()][posn.getY()] = "+";
    }
  }

  /**
   * place all tiles down between the waypoints
   * @param segments the tile segements to place between the points
   * @param allPoints the points to lay the segments between
   */
  private void addSegements(List<ArrayList<Tile>> segments, List<Posn> allPoints) {
    for (int i = 0; i < segments.size(); i++) {
      ArrayList<Tile> tileSegement = segments.get(i);
      Posn start = allPoints.get(i);
      Posn end = allPoints.get(i + 1);
      // check if the tiles should be laid horizontally or vertically
      if (start.getX() == end.getX()) {
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(t);
          // check if tiles should be laid up or down
          int direction = calcDirection(start.getY(), end.getY());
          this.levelGrid[start.getX()][start.getY() + ((t + 1) * direction)] = tile.toString();
        }
      } else if (start.getY() == end.getY()) {
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(i);
          // check if tiles should be laid right or left
          int direction = calcDirection(start.getX(), end.getX());
          this.levelGrid[start.getX() + ((t + 1) * direction)][start.getY()] = tile.toString();
        }
      }
    }
  }

  /**
   * returns the direction (+/-) to place the segment tiles
   * @param start point
   * @param end point
   */
  private int calcDirection(int start, int end) {
    return start > end ? -1 : 1;
  }

  /**
   * populates the levelGrid with rooms using the List<Rooms> field
   */
  private void addRooms() {
    for (int i = 0; i < this.rooms.size(); i++) {
      Room room = this.rooms.get(i);
      List<ArrayList<String>> roomGrid = room.renderRoom();
      Posn upperLeft = room.getUpperLeft();
      for (int x = 0; x < room.getxDim(); x++) {
        for (int y = 0; y < room.getyDim(); y++) {
          this.levelGrid[x + upperLeft.getX()][y + upperLeft.getY()] = roomGrid.get(x).get(y);
        }
      }
    }
  }

  /**
   * Creates the ASCII string representation of a Level and all its data
   */
  public String createLevelString() {
    this.initLevelGrid();
    String levelAcc = "";
    for (int i = 0; i < levelX; i++) {
      for (int j = 0; j < levelY; j++) {
        levelAcc += this.levelGrid[i][j];
      }
      if (i != this.levelGrid.length - 1) {
        levelAcc += "\n";
      }
    }
    return levelAcc;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }

  public List<Hallway> getHallways() {
    return hallways;
  }

  public void setHallways(List<Hallway> hallways) {
    this.hallways = hallways;
  }


}
