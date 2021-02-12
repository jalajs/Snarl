import java.util.ArrayList;
import java.util.List;

public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;


  public Level() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
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

  static boolean levelValid() {
    // check the upper-left Cartesian postions of each rooms and determine if any rooms in this level
    // overlap
    return false;
   }
}
