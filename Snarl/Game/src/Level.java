import java.util.ArrayList;
import java.util.List;
// represents a game level, including all rooms, hallways, dimensions, and the grid
// an example level could look like this
//     ...    ...
//     ..X    ...               this level has 4 rooms and 3 hallways
//     ..|....|.|
//              .               KEY
//              .               . = unoccuppied tile
//              .               X = wall
//     ...|.....+               + = waypoint
//     XX.X                     | = door
//     ....                     empty space = no tiles
//     ...|
//        .
//        .
//        .
//        +...|..
//            ..|
public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;
  private int levelX;
  private int levelY;
  private String[][] levelGrid;

  // this basic no input constructor creates an empty 10x10 level (mostly used for testing)
  public Level() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelX = 10;
    this.levelY = 10;
    this.levelGrid = new String[levelX][levelY];
    this.initGridSpace();
    }

  // this constructor allows for manual setting of x & y dimensions
  public Level(int levelX, int levelY) {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelX = levelX;
    this.levelY = levelY;
    this.levelGrid = new String[this.levelX][this.levelY];
    this.initGridSpace();
  }

  // populates the entire grid of the level with an initial value of "  "
  private void initGridSpace() {
    for (int i = 0; i < this.levelX; i++) {
      for (int j = 0; j < this.levelY; j++) {
        this.levelGrid[i][j] = " ";
      }
    }
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

  // populate our level grid with rooms (and hallways)
  private void initLevelGrid() {
    this.addRooms();
    this.addHallways();
  }


  // populates the levelGrid with hallways using the List<Hallway> in this level
  private void addHallways() {
    for (Hallway hallway : this.hallways) {
      List<Posn> waypoints = hallway.getWaypoints();
      List<ArrayList<Tile>> segments = hallway.getTileSegments();
      List<Door> doors = hallway.getDoors();
      // populate waypoints
      for (Posn posn : waypoints) {
        this.levelGrid[posn.getX()][posn.getY()] = "+";
      }

      // add door to list of points
      waypoints.add(0, doors.get(0).getTileCoord());
      waypoints.add(doors.get(1).getTileCoord());

      // populate tiles between waypoints
      for (int i = 0; i < segments.size(); i++) {
        ArrayList<Tile> tileSegement = segments.get(i);
        Posn start = waypoints.get(i);
        Posn end = waypoints.get(i + 1);

        if (start.getX() == end.getX()) {
          for (int t = 0; t < tileSegement.size(); t++) {
            Tile tile = tileSegement.get(t);

            int direction = 1;
            if (start.getY() > end.getY()) {
              direction = -1;
            }

            this.levelGrid[start.getX()][start.getY() + ((t + 1) * direction)] = tile.toString();
          }
        } else if (start.getY() == end.getY()) {
          for (int t = 0; t < tileSegement.size(); t++) {
            Tile tile = tileSegement.get(i);

            int direction = 1;
            if (start.getX() > end.getX()) {
              direction = -1;
            }

            this.levelGrid[start.getX() + ((t + 1) * direction)][start.getY()] = tile.toString();
          }
        }
      }
    }
  }

  // populates the levelGrid with rooms using the List<Rooms> field
  private void addRooms() {
    for (int i = 0; i < this.rooms.size(); i++) {
      Room room = this.rooms.get(i);
      List<ArrayList<String>> roomGrid = room.renderRoom();
      Posn upperLeft = room.getUpperLeft();
      for (int x = 0; x < room.getxDim(); x++) {
        // System.out.println("x:" + x);

        for (int y = 0; y < room.getyDim(); y++) {
          //System.out.println("y: " + y);

          this.levelGrid[x + upperLeft.getX()][y + upperLeft.getY()] = roomGrid.get(x).get(y);
        }
      }
    }
  }

  public boolean levelValid() {
    // check the upper-left Cartesian postions of each rooms and determine if any rooms in this level
    // overlap
    return false;
  }

  public void drawLevel() {
    String levelString = createLevelString();
    System.out.print(levelString);
  }

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


}
