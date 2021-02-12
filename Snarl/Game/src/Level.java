import java.util.ArrayList;
import java.util.List;

public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;
  private int levelX;
  private int levelY;
  private String[][] levelGrid;


  public Level() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelX = 10;
    this.levelY = 10;
    this.levelGrid = new String[levelX][levelY];
    for (int i = 0; i < levelX; i++) {
      for (int j = 0; j < levelY; j++) {
        levelGrid[i][j] = " ";
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

    // Todo: add hallways to level grid
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
    for (int i = 0; i < levelX; i ++) {
      for (int j = 0; j < levelY; j ++) {
        levelAcc += this.levelGrid[i][j];
      }
      if (i != this.levelGrid.length - 1) {
        levelAcc += "\n";
      }
    }
    return levelAcc;
  }


}
