package GameObjects;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Door;
import GameObjects.Posn;

/**
 * Represents a hallway in a level in our game. A hallway has two rooms to connect and a list of
 * waypoints, which allows for corners in the hallway. Hallways are comprised of "segments" of
 * tiles.
 */
public class Hallway {
  private List<Posn> wayPoints;
  private List<Door> doors;
  private List<ArrayList<Tile>> tileSegments;

  public Hallway() {
    this.wayPoints = new ArrayList<>();
    this.tileSegments = new ArrayList<>();

  }

  public void connectDoorsToRooms(List<Room> rooms) {
    for (Door door : this.doors) {
      for (Room room : rooms) {
        List<Door> roomDoors = room.getDoorPositions();
        int roomOriginX = room.getUpperLeft().getX();
        int roomOriginY = room.getUpperLeft().getY();
        for (Door roomDoor : roomDoors) {
          Posn doorPosnRelToLevel = new Posn(roomDoor.getTileCoord().getX() + roomOriginX,
                  roomDoor.getTileCoord().getY() + roomOriginY);
          if (door.getTileCoord().equals(doorPosnRelToLevel)) {
            door.setRoom(room);
          }
        }
      }
    }
  }

  /**
   * Checks all the rooms reachable from the hallway.
   *
   * @return a list of the upperLeft posns of the reachable rooms
   */
  public List<Posn> checkReachableRooms(List<Posn> reachableAcc, List<Room> visited) {
    System.out.println("hallway check reachable rooms");
    for (Door door : this.doors) {
      door.getRoom().checkReachableRooms(reachableAcc, visited);
    }
    return reachableAcc;
  }

  /**
   * Determines whether the given point is located somewhere in this hallway
   *
   * @param point the given posn
   * @return whether or not the posn is in this hallway
   */
  public boolean isPosnInHallway(Posn point) {
    for (Posn posn : wayPoints) {
      if (posn.equals(point)) {
        return true;
      }
    }
    List<Posn> allPoints = this.getWaypoints();
    allPoints.add(doors.get(0).getTileCoord());
    allPoints.add(doors.get(1).getTileCoord());

    for (int i = 0; i < allPoints.size() - 1; i++) {
      Posn from = allPoints.get(i);
      Posn to = allPoints.get(i + 1);
      if (to.getX() == from.getX()) {
        // vertical segment
        // if the points x = from x and the y is between the to and from
        if (point.getX() == from.getX() && isPointInRange(point.getY(), from.getY(), to.getY())) {
          return true;
        }
      }
      if (to.getY() == from.getY()) {
        // horizontal segment
        if (point.getY() == from.getY() && isPointInRange(point.getX(), from.getX(), to.getX())) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * checks if the given point value is between the from or to value
   *
   * @param point int to check if in the range
   * @param from  edge of range
   * @param to    edge of range
   * @return boolean if in range
   */
  private boolean isPointInRange(int point, int from, int to) {
    return (point >= from && point <= to) || (point <= from && point >= to);
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
   * Populates the hallway with tiles between the rooms it connects
   */

  private void initHallway() {

  }

  /**
   * Determines if this hallways is valid. A hallway is valid if it connects two rooms at its
   * endpoints. Each segment as delimited by subsequent points1) is either horizontal or vertical
   * (i.e., perpendicular with the x or y axis).
   *
   * @return whether or not this hallway is valid
   */
  public static boolean isValidHallway() {
    return true;
  }

}
