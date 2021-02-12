import java.util.ArrayList;
import java.util.List;

public class Hallway {
  private List<Posn> endPoints;
  private List<Door> roomsConnected;


  public Hallway() {
    this.endPoints = new ArrayList<>();

  }

  public List<Posn> getWaypoints() {
    return endPoints;
  }

  public void setWaypoints(List<Posn> waypoints) {
    this.endPoints = waypoints;
  }

  public List<Door> getRoomsConnected() {
    return roomsConnected;
  }

  public void setRoomsConnected(List<Door> roomsConnected) {
    this.roomsConnected = roomsConnected;
  }

  public static boolean isValidHallway() {
    // a hallway is valid if it connects two rooms at its endpoints. each segment
    // (as delimited by subsequent points1) is either horizontal or vertical (i.e., perpendicular with the x or y axis).
    return false;
  }

  public String representHallway() {
    return "X";
  }
}
