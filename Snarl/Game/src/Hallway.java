import java.util.ArrayList;
import java.util.List;

public class Hallway {
  private List<Posn> wayPoints;
  private List<Door> doors;
  private List<ArrayList<Tile>> tileSegments;

  public Hallway() {

    this.wayPoints = new ArrayList<>();
    this.tileSegments = new ArrayList<>();

  }

  public List<ArrayList<Tile>> getTileSegments() {
    return tileSegments;
  }

  public void setTileSegments(List<ArrayList<Tile>> tileSegments) {
    this.tileSegments = tileSegments;
  }

  public List<Posn> getWaypoints() {
    return this.wayPoints;
  }

  public void setWaypoints(List<Posn> waypoints) {
    this.wayPoints = waypoints;
  }

  public List<Door> getDoors() {
    return doors;
  }

  public void setDoors(List<Door> doors) {
    this.doors = doors;
  }


  // populate the hallway with tiles between the rooms it connects
  private void initHallway() {

  }

  public static boolean isValidHallway() {
    // a hallway is valid if it connects two rooms at its endpoints. each segment
    // (as delimited by subsequent points1) is either horizontal or vertical (i.e., perpendicular with the x or y axis).
    return true;
  }

}
