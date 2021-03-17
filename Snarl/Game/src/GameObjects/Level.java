package GameObjects;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;

import GameObjects.Actor;
import GameObjects.Door;
import GameObjects.ExitKey;
import GameObjects.Hallway;
import GameObjects.Posn;

/**
 * represents a game level, including all rooms, hallways, dimensions A representation is produced
 * and tested in .../test/LevelRepresentationTest.java
 */
public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;
  private int levelX;
  private int levelY;
  private String[][] levelGrid;
  private Tile[][] tileGrid;
  private Posn exitKeyPosition;
  private Posn exitDoorPosition;

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
    this.tileGrid = new Tile[levelX][levelY];
    this.initEmptyTileGrid();
    this.exitKeyPosition = new Posn(-1, -1);
    this.exitDoorPosition = new Posn(-1, -1);
  }

  /**
   * this constructor allows for manual setting of x & y dimensions
   *
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
    this.tileGrid = new Tile[this.levelX][this.levelY];
    this.initEmptyTileGrid();
    this.exitKeyPosition = new Posn(-1, -1);
    this.exitDoorPosition = new Posn(-1, -1);
  }

  /**
   * This constructor builds a level given a list of rooms, hallways, and exit and key positions. It
   * is assumed that the rooms, hallways, and posns are wellformed. This constructor is used
   * primarily by the JSON tests, which is why the level perimeters are pre-set.
   *
   * @param rooms           list of rooms to be placed in the level
   * @param hallways        list of hallways to be placed in the level
   * @param exitAndKeyPosns array of posns ordered like this: (key, exit)
   */
  public Level(List<Room> rooms, List<Hallway> hallways, List<Posn> exitAndKeyPosns) {
    this.levelX = 101;
    this.levelY = 101;
    this.levelGrid = new String[this.levelX][this.levelY];
    this.initGridSpace();
    this.tileGrid = new Tile[this.levelX][this.levelY];
    this.initEmptyTileGrid();

    Posn keyPosn = exitAndKeyPosns.get(0);
    Posn exitPosn = exitAndKeyPosns.get(1);

    this.rooms = rooms;
    this.hallways = hallways;
    this.connectUpDoors();
    this.initGrid();

    this.dropKey(keyPosn);
    this.createLevelExit(exitPosn);
  }

  /**
   * This constructor builds a level given a list of rooms, hallways, and exit and key positions. It
   * is assumed that the rooms, hallways, and posns are wellformed. This constructor is used
   * primarily by the JSON tests, which is why the level perimeters are pre-set.
   *
   * @param rooms           list of rooms to be placed in the level
   * @param hallways        list of hallways to be placed in the level
   * @param exitAndKeyPosns array of posns ordered like this: (key, exit)
   * @param exitLocked      represents whether or not the exit is locked and a key is in the
   *                        exitAndKeyPosns
   */
  public Level(List<Room> rooms, List<Hallway> hallways, List<Posn> exitAndKeyPosns, boolean exitLocked) {
    this.levelX = 101;
    this.levelY = 101;
    this.levelGrid = new String[this.levelX][this.levelY];
    this.initGridSpace();
    this.tileGrid = new Tile[this.levelX][this.levelY];
    this.initEmptyTileGrid();
    if (!exitLocked) {
      Posn exitPosn = exitAndKeyPosns.get(0);

      this.rooms = rooms;
      this.hallways = hallways;
      this.connectUpDoors();
      this.initGrid();

      this.createLevelExit(exitPosn);
    } else {
      Posn keyPosn = exitAndKeyPosns.get(0);
      Posn exitPosn = exitAndKeyPosns.get(1);

      this.rooms = rooms;
      this.hallways = hallways;
      this.connectUpDoors();
      this.initGrid();

      this.dropKey(keyPosn);
      this.createLevelExit(exitPosn);
    }

  }

  /**
   * connects the doors in the hallways and rooms to eachother
   */
  private void connectUpDoors() {
    for (Room room : this.rooms) {
      room.connectDoorsToHallways(this.hallways);
    }
    for (Hallway hallway : this.hallways) {
      hallway.connectDoorsToRooms(this.rooms);
    }
  }

  /**
   * Initializes the tile grid with non-traversable tiles. This is the state of the tile grid before
   * the rooms or hallways are added.
   */
  private void initEmptyTileGrid() {
    for (int x = 0; x < this.levelX; x++) {
      for (int y = 0; y < this.levelY; y++) {
        Tile tile = new Tile(true);
        tile.setPosition(new Posn(x, y));
        tileGrid[x][y] = tile;
      }
    }
  }

  /**
   * Populates the entire grid of the level with an initial value of "  "
   */
  private void initGridSpace() {
    for (int i = 0; i < this.levelX; i++) {
      for (int j = 0; j < this.levelY; j++) {
        this.levelGrid[i][j] = " ";
      }
    }
  }

  /**
   * Create a level exit at the given position in the level.
   *
   * @param levelExitPosn indicates where the level exit should be places
   */
  public void createLevelExit(Posn levelExitPosn) {
    Door levelExit = new Door();
    levelExit.setTileCoord(levelExitPosn);
    levelExit.setLevelExit(true);

    int xValue = levelExitPosn.getX();
    int yValue = levelExitPosn.getY();

    tileGrid[xValue][yValue].setDoor(levelExit);
    levelGrid[xValue][yValue] = levelExit.toString();
    this.exitDoorPosition = levelExitPosn;
  }

  /**
   * Place key on the given position in the level. Also sets this.exitKeyPosition to the given
   * posn.
   *
   * @param keyPosition indicates where the key should be placed
   * @return map containing the key object mapped to its position
   */
  public void dropKey(Posn keyPosition) {
    int xValue = keyPosition.getX();
    int yValue = keyPosition.getY();
    Posn exitKeyPosition = new Posn(xValue, yValue);
    this.exitKeyPosition = exitKeyPosition;
    ExitKey exitKey = new ExitKey(exitKeyPosition);
    tileGrid[xValue][yValue].setCollectable(exitKey);
    levelGrid[exitKeyPosition.getX()][exitKeyPosition.getY()] = exitKey.toString();
  }


  /**
   * Spawn players and adversaries in the level. Players are placed in the first most room, and
   * Adversaries are placed in the last room.
   *
   * @return map containing the actor objects mapped to their positions
   */
  public void spawnActors(List<Actor> players, List<Actor> adversaries) {
    // find the first and last rooms for placing the players and adversaries
    Room firstRoom = this.rooms.get(0);
    Room lastRoom = this.rooms.get(this.rooms.size() - 1);
    // place actors on traversable tiles in the first room.
    this.placeActorsInRoom(firstRoom, players);
    // place adversaries on traversable tiles in the last room
    this.placeActorsInRoom(lastRoom, adversaries);
  }

  /**
   * Helper method that places the given actors in traversable tiles in the given room. Players are
   * placed starting in the upper left corner.
   *
   * @param room   the given room the actors should be places
   * @param actors the given actors that need to be placed
   */
  private void placeActorsInRoom(Room room, List<Actor> actors) {
    Posn levelPosition = new Posn(0, 0);
    for (Actor actor : actors) {
      levelPosition = firstUnoccupiedTilePosition(room);
      actor.setPosition(levelPosition);
      tileGrid[levelPosition.getX()][levelPosition.getY()].setOccupier(actor);
      levelGrid[levelPosition.getX()][levelPosition.getY()] = actor.representation();
    }
  }

  /**
   * Helper method to find the first unoccupied tile in a room (from upper left) to place actors
   *
   * @param room the room in which to find the position
   * @return the position of the first tile that is unoccupied
   */
  private Posn firstUnoccupiedTilePosition(Room room) {
    Posn upperLeft = room.getUpperLeft();
    Posn levelPosition = new Posn(-1, -1);
    for (int roomX = 0; roomX < room.getxDim(); roomX++) {
      for (int roomY = 0; roomY < room.getyDim(); roomY++) {
        String tileString = levelGrid[roomX + upperLeft.getX()][roomY + upperLeft.getY()];
        if (tileString.equals(".")) {
          levelPosition = new Posn(roomX + upperLeft.getX(), roomY + upperLeft.getY());
          return levelPosition;
        }
      }
    }
    return levelPosition;
  }

  /**
   * Place the actors on their position in the GameObjects.Level
   *
   * @param actors map of actors that know their positions in the level
   */
  public void placeActorsInLevel(List<Actor> actors) {
    for (Actor actor : actors) {
      Posn posn = actor.getPosition();
      tileGrid[posn.getX()][posn.getY()].setOccupier(actor);
      levelGrid[posn.getX()][posn.getY()] = actor.representation();
      actor.setPosition(posn);
    }
  }

  /**
   * Removes the key from this level.
   */
  public void removeKey() {
    Posn exitKeyPos = this.exitKeyPosition;
    this.tileGrid[exitKeyPos.getX()][exitKeyPos.getY()].setCollectable(null);
    this.levelGrid[exitKeyPos.getX()][exitKeyPos.getY()] = ".";
  }

  /**
   * Expels the given player from the level
   *
   * @param expelledPlayer the player to be expelled
   */
  public void expelPlayer(Actor expelledPlayer) {
    Posn expelPosition = expelledPlayer.getPosition();
    this.tileGrid[expelPosition.getX()][expelPosition.getY()].setOccupier(null);
    this.levelGrid[expelPosition.getX()][expelPosition.getY()] = ".";
  }

  /**
   * This method updates the level object to reflect a players move. This method assumes the move
   * has already been deemed valid by the Rule Component.
   *
   * @param p           is the player that has moved
   * @param newPosition is the new position of the move
   * @return if the player has landed on exit or not
   */
  public Tile handlePlayerMove(Actor p, Posn newPosition) {
    Posn prevPosition = p.getPosition();
    // remove player from old position
    tileGrid[prevPosition.getX()][prevPosition.getX()].setOccupier(null);
    levelGrid[prevPosition.getX()][prevPosition.getY()] = ".";
    // add player to new position if there no current occupier
    if (tileGrid[newPosition.getX()][newPosition.getY()].getOccupier() == null) {
      tileGrid[newPosition.getX()][newPosition.getY()].setOccupier(p);
      levelGrid[newPosition.getX()][newPosition.getY()] = "O";
      p.setPosition(newPosition);
    }
    // check if player's new position is on Exit
    return tileGrid[newPosition.getX()][newPosition.getY()];
  }


  /**
   * Creates the ASCII string representation of a GameObjects.Level and all its data. Assumes the
   * grid has already be initialized.
   */
  public String createLevelString() {
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

  /**
   * populate our level/tile grid with rooms (and hallways)
   */
  public void initGrid() {
    this.addRooms();
    this.addHallways();
  }

  /**
   * populates the levelGrid with rooms using the List<GameObjects.Room> field
   */
  private void addRooms() {
    for (int i = 0; i < this.rooms.size(); i++) {
      Room room = this.rooms.get(i);
      String[][] roomGrid = room.renderRoom();
      Tile[][] roomTileGrid = room.getTileGrid();
      Posn upperLeft = room.getUpperLeft();
      for (int x = 0; x < room.getxDim(); x++) {
        for (int y = 0; y < room.getyDim(); y++) {
          this.levelGrid[x + upperLeft.getX()][y + upperLeft.getY()] = roomGrid[x][y];
          this.tileGrid[x + upperLeft.getX()][y + upperLeft.getY()] = roomTileGrid[x][y];
        }
      }
    }
  }

  /**
   * populates the levelGrid with hallways using the List<GameObjects.Hallway> in this level
   */
  private void addHallways() {
    for (Hallway hallway : this.hallways) {
      List<Posn> waypoints = hallway.getWaypoints();
      List<ArrayList<Tile>> segments = hallway.getTileSegments();  // list of segments
      List<Door> doors = hallway.getDoors();
      // populate waypoints
      this.addWayPoints(waypoints);

      // add door to list of waypoints
      waypoints.add(0, doors.get(0).getTileCoord());
      waypoints.add(doors.get(1).getTileCoord());
      // add tiles between waypoints
      addSegements(segments, waypoints);

    }
  }

  /**
   * place waypoints in level and tile grid
   *
   * @param waypoints the positions of the waypoints to be placed in the grid
   */
  private void addWayPoints(List<Posn> waypoints) {
    for (Posn posn : waypoints) {
      this.levelGrid[posn.getX()][posn.getY()] = "+";
      Tile wayPointTile = new Tile(false);
      wayPointTile.setPosition(new Posn(posn.getX(), posn.getY()));
      this.tileGrid[posn.getX()][posn.getY()] = wayPointTile;
    }
  }

  /**
   * place all tiles down between the waypoints
   *
   * @param segments  the tile segements to place between the points
   * @param allPoints the points to lay the segments between
   */
  private void addSegements(List<ArrayList<Tile>> segments, List<Posn> allPoints) {
    for (int i = 0; i < segments.size(); i++) {
      ArrayList<Tile> tileSegement = segments.get(i);
      Posn start = allPoints.get(i);
      Posn end = allPoints.get(i + 1);

      // check if the tiles should be laid horizontally or vertically
      if (start.getX() == end.getX()) {
        // check if tiles should be laid up or down
        int direction = calcDirection(start.getY(), end.getY());
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(t);
          tile.setPosition(new Posn(start.getX(), start.getY() + ((t + 1) * direction)));
          this.levelGrid[start.getX()][start.getY() + ((t + 1) * direction)] = tile.toString();
          this.tileGrid[start.getX()][start.getY() + ((t + 1) * direction)] = tile;
        }
      } else if (start.getY() == end.getY()) {
        // check if tiles should be laid right or left
        int direction = calcDirection(start.getX(), end.getX());
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(t);
          tile.setPosition(new Posn(start.getX() + ((t + 1) * direction), start.getY()));
          // start = 0, t = 0, direction = -1
          this.levelGrid[start.getX() + ((t + 1) * direction)][start.getY()] = tile.toString();
          this.tileGrid[start.getX() + ((t + 1) * direction)][start.getY()] = tile;
        }
      }
    }
  }

  /**
   * returns the direction (+/-) to place the segment tiles
   *
   * @param start point
   * @param end   point
   */
  private int calcDirection(int start, int end) {
    return start > end ? -1 : 1;
  }


  /**
   * Helper method to generates the position for a random unoccupied tile. Unused currently but will
   * likely be needed for future milestones.
   *
   * @return GameObjects.Posn of random unoccupied tile
   */
  private Posn generateRandomUnoccupiedTile() {
    Posn posn = new Posn(-1, -1);
    Random random = new Random();
    int counter = 0;
    while (counter < levelX * levelY) {
      int randomX = random.nextInt(levelX);
      int randomY = random.nextInt(levelY);
      String tileString = levelGrid[randomX][randomY];
      if (tileString.equals(".")) {
        posn.setX(randomX);
        posn.setY(randomY);
        break;
      }
      counter++;
    }
    return posn;
  }

  /**
   * Check whether the given point is traversable (door or "floor" or in a hallway)
   *
   * @param point the given posn
   * @return if the point is traversable or not
   */
  public boolean checkTraversable(Posn point) {
    int x = point.getX();
    int y = point.getY();
    if (this.levelY <= y || this.levelX <= x) {
      return false;
    }
    if (this.tileGrid[x][y] == null) {
      return false;
    }
    Tile tile = this.tileGrid[x][y];
    return !tile.getisWall();
  }

  /**
   * Returns the type of object occupying the tile at the given point.
   *
   * @param point the posn to check for an object
   * @return either "key", "door" or "null"
   */
  public String checkObjectType(Posn point) {
    String tileRep = levelGrid[point.getX()][point.getY()];
    if (tileRep.equals("|")) {
      return tileGrid[point.getX()][point.getY()].getDoor().isLevelExit() ? "exit" : "null";
    } else if (tileRep.equals("K")) {
      return "key";
    } else {
      return "null";
    }
  }

  /**
   * Checks if the given posn in a room or a hallway, or neither.
   *
   * @param point the posn to be checked
   * @return "hallway", "room", or "void" if posn is in neither
   */
  public String checkSegmentType(Posn point) {
    for (Room room : this.rooms) {
      if (room.isPosnInRoom(point)) {
        return "room";
      }
    }
    for (Hallway hallway : this.hallways) {
      if (hallway.isPosnInHallway(point)) {
        return "hallway";
      }
    }
    return "void";
  }

  /**
   * Checks what rooms are reachable from the given point
   *
   * @param point       the given point
   * @param segmentType the type of segment the point lies on
   * @return The origins for the rooms you can immediately reach from the given point
   */
  public List<Posn> checkReachable(Posn point, String segmentType) {
    List<Posn> reachablePosns = new ArrayList<>();
    if (segmentType.equals("room")) {
      for (Room room : this.rooms) {
        if (room.isPosnInRoom(point)) {
          reachablePosns = room.checkReachableRooms();
        }
      }
    } else {
      for (Hallway hallway : this.hallways) {
        if (hallway.isPosnInHallway(point)) {
          reachablePosns = hallway.checkReachableRooms();
        }
      }
    }
    return reachablePosns;
  }

  /**
   * A level is over if the exit door is unlocked and a player goes through it or if all players in
   * the level are expelled.
   *
   * @return whether or not the given Level is over or not
   */
  public boolean isLevelEnd() {
    return false;
  }

  /**
   * Invalid if too far, wall, empty space (no tile) or interaction is invalid A move is to far if
   * the desired tile is more than two cardinal moves away
   *
   * @param actor       the given actor moving
   * @param destination the destination it is trying to move to
   * @return whether or not there move is valid
   */
  public boolean canActorMoveHere(Actor actor, Posn destination) {
    boolean isDestinationTraversable = checkTraversable(destination);
    if (!isDestinationTraversable) {
      return false;
    }
    // check if the player is any of the rooms. If so, check the possible cardinal moves
    // from the players position in the room and see if the destination is one of them
    for (Room room : this.rooms) {
      if (room.isPosnInRoom(actor.getPosition())) {
        List<Posn> oneCardinalMove = room.getNextPossibleCardinalMoves(actor.getPosition());
        List<Posn> allCardinalMoves = new ArrayList(oneCardinalMove);
        for (Posn cardinalMove : oneCardinalMove) {
          allCardinalMoves.addAll(room.getNextPossibleCardinalMoves(cardinalMove));
        }
        return allCardinalMoves.contains(destination);
      }
    }
    // check if the player is any of the rooms. If so, check the possible cardinal moves
    // from the players position in the room and see if the destination is one of them
    for (Hallway hallway : this.hallways) {
      if (hallway.isPosnInHallway(actor.getPosition())) {
        List<Posn> oneCardinalMove = hallway.getNextPossibleCardinalMoves(actor.getPosition());
        List<Posn> allCardinalMoves = new ArrayList(oneCardinalMove);
        for (Posn cardinalMove : oneCardinalMove) {
          allCardinalMoves.addAll(hallway.getNextPossibleCardinalMoves(cardinalMove));
        }
        return allCardinalMoves.contains(destination);
      }
    }
    // if none of the above conditions are true, then actor cannot move here
    return false;
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

  public int getLevelX() {
    return levelX;
  }

  public void setLevelX(int levelX) {
    this.levelX = levelX;
  }

  public int getLevelY() {
    return levelY;
  }

  public void setLevelY(int levelY) {
    this.levelY = levelY;
  }

  public String[][] getLevelGrid() {
    return levelGrid;
  }

  public void setLevelGrid(String[][] levelGrid) {
    this.levelGrid = levelGrid;
  }

  public Tile[][] getTileGrid() {
    return tileGrid;
  }

  public void setTileGrid(Tile[][] tileGrid) {
    this.tileGrid = tileGrid;
  }

  public Posn getExitKeyPosition() {
    return exitKeyPosition;
  }

  public void setExitKeyPosition(Posn exitKeyPosition) {
    this.exitKeyPosition = exitKeyPosition;
  }

  public Posn getExitDoorPosition() {
    return exitDoorPosition;
  }

  public void setExitDoorPosition(Posn exitDoorPosition) {
    this.exitDoorPosition = exitDoorPosition;
  }
}
