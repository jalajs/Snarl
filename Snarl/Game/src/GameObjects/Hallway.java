package GameObjects;

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
      if (isPosnInSegment(to, from, point)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if the given Posn point lies in the segment between to and from
   *
   * @param to the end waypoint of the segment
   * @param from the from waypoint of the segment
   * @param point the given posn
   * @return
   */
  private boolean isPosnInSegment(Posn to, Posn from, Posn point) {
    if (to.getRow() == from.getRow()) {
      // vertical segment
      // if the points' row = from row and the col is between the to and from
      if (point.getRow() == from.getRow() && isPointInRange(point.getCol(), from.getCol(), to.getCol())) {
        return true;
      }
    }
    if (to.getCol() == from.getCol()) {
      // horizontal segment
      if (point.getCol() == from.getCol() && isPointInRange(point.getRow(), from.getRow(), to.getRow())) {
        return true;
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

   List<Posn> possibleMoves = this.generatePossibleMoves(position);

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

  /**
   * Create a list of possible positions a person can move from the given position in a hallway
   * @param position the origin position
   * @return An enumeration of possible moves
   */
  private List<Posn> generatePossibleMoves(Posn position) {
    List<Posn> possibleMoves = new ArrayList<>();
    int row = position.getRow();
    int col = position.getCol();
    possibleMoves.add(new Posn(row, col + 1));
    possibleMoves.add(new Posn(row, col - 1));
    possibleMoves.add(new Posn(row + 1, col));
    possibleMoves.add(new Posn(row - 1, col + 1));

    return possibleMoves;
  }
}

