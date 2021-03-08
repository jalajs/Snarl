package GameObjects;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * This method serves to connect the hallways doors to its connecting room. This is done by
   * finding the room the door connects to by using the posns of each rooms' doors
   *
   * @param rooms are all the rooms in the level
   */
  public void connectDoorsToRooms(List<Room> rooms) {
    for (Door door : this.doors) {
      for (Room room : rooms) {
        List<Door> roomDoors = room.getDoorPositions();
        for (Door roomDoor : roomDoors) {
          Posn doorPosnRelToLevel = room.posnRelativeToLevel(roomDoor.getTileCoord());
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
  public List<Posn> checkReachableRooms() {
    List<Posn> reachableRoomUpperLefts = new ArrayList<>();
    for (Door door : this.doors) {
      Room doorRoom = door.getRoom();
      reachableRoomUpperLefts.add(doorRoom.getUpperLeft());
    }
    return reachableRoomUpperLefts;
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

  /**
   * Returns the position in front of the given and behind it in the hallway
   *
   * @param position the position from which to calculate the other moves
   * @return
   */
  public List<Posn> getNextPossibleCardinalMoves(Posn position) {
    List<Posn> cardinalMoves = new ArrayList<>();

    List<Posn> allPoints = new ArrayList<>();
    allPoints.add(doors.get(0).getTileCoord());
    allPoints.addAll(this.getWaypoints());
    allPoints.add(doors.get(1).getTileCoord());

    List<Posn> possibleMoves = new ArrayList<>();
    possibleMoves.add(new Posn(position.getX(), position.getY() + 1));
    possibleMoves.add(new Posn(position.getX(), position.getY() - 1));
    possibleMoves.add(new Posn(position.getX() + 1, position.getY()));
    possibleMoves.add(new Posn(position.getX() - 1, position.getY() + 1));

    for (Posn wayPointOrDoor : allPoints) {
      if (possibleMoves.contains(wayPointOrDoor)) {
        cardinalMoves.add(wayPointOrDoor);
      }
    }
    for (List<Tile> segment : tileSegments) {
      for (Tile tile : segment) {
        Posn tilePosition = tile.getPosition();
        if (possibleMoves.contains(tilePosition)) {
          cardinalMoves.add(tilePosition);
        }
      }
    }

    return cardinalMoves;
  }
}

