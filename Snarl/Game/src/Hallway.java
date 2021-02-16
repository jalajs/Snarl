import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hallway in a level in our game. A hallway has two rooms to connect and a list of waypoints,
 * which allows for corners in the hallway. Hallways are comprised of "segments" of tiles.
 */
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


  /**
   *  Populates the hallway with tiles between the rooms it connects
   */

  private void initHallway() {

  }

  /**
   * Determines if this hallways is valid.
   * A hallway is valid if it connects two rooms at its endpoints.
   * Each segment as delimited by subsequent points1) is either horizontal or vertical (i.e., perpendicular with the x or y axis).
   * @return whether or not this hallway is valid
   */
  public static boolean isValidHallway() {
    return true;
  }

}
