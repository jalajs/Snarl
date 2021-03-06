package GameObjects;

import java.util.*;

/**
 * represents a game level, including all rooms, hallways, dimensions A representation is produced
 * and tested in .../test/LevelRepresentationTest.java
 */
public class Level {
  private List<Room> rooms;
  private List<Hallway> hallways;
  private int levelRows;
  private int levelCols;
  private String[][] levelGrid;
  private Tile[][] tileGrid;
  private Posn exitKeyPosition;
  private Posn exitDoorPosition;
  private Random rand;

  /**
   * this basic no input constructor creates an empty 10x10 level (mostly used for testing)
   */
  public Level() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelRows = 10;
    this.levelCols = 10;
    this.levelGrid = new String[levelRows][levelCols];
    this.initGridSpace();
    this.tileGrid = new Tile[levelRows][levelCols];
    this.initEmptyTileGrid();
    this.exitKeyPosition = new Posn(-1, -1);
    this.exitDoorPosition = new Posn(-1, -1);
    this.rand = new Random();
  }

  /**
   * this constructor allows for manual setting of row & col dimensions
   *
   * @param levelRows the number of rows in the level
   * @param levelCols the number of columns in the level
   */
  public Level(int levelRows, int levelCols) {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
    this.levelRows = levelRows;
    this.levelCols = levelCols;
    this.levelGrid = new String[this.levelRows][this.levelCols];
    this.initGridSpace();
    this.tileGrid = new Tile[this.levelRows][this.levelCols];
    this.initEmptyTileGrid();
    this.exitKeyPosition = new Posn(-1, -1);
    this.exitDoorPosition = new Posn(-1, -1);
    this.rand = new Random();
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
    this.rooms = rooms;
    this.hallways = hallways;

    this.setDimensionsAndGrids(rooms, hallways);

    Posn keyPosn = exitAndKeyPosns.get(0);
    Posn exitPosn = exitAndKeyPosns.get(1);

    this.connectUpDoors();
    this.initGrid();

    this.dropKey(keyPosn);
    this.createLevelExit(exitPosn);
    this.rand = new Random();
  }

  private void setDimensionsAndGrids(List<Room> rooms, List<Hallway> hallways) {
    int maxRow = this.getMaxRow();
    int maxCol = this.getMaxCol();
    this.levelRows = maxRow;
    this.levelCols = maxCol;
    this.levelGrid = new String[this.levelRows][this.levelCols];
    this.initGridSpace();
    this.tileGrid = new Tile[this.levelRows][this.levelCols];
    this.initEmptyTileGrid();
  }


  private int getMaxRow() {
    int maxRow = 0;
    for (Hallway hallway : hallways) {
      for (Posn waypoint : hallway.getWaypoints()) {
        if (maxRow < waypoint.getRow()) {
          maxRow = waypoint.getRow();
        }
      }
    }
    for (Room room : rooms) {
      int maxRoomRow = room.getUpperLeft().getRow() + room.getRows();
      if (maxRow < maxRoomRow) {
        maxRow = maxRoomRow;
      }
    }
    return maxRow + 5;
  }

  private int getMaxCol() {
    int maxCol = 0;
    for (Hallway hallway : hallways) {
      for (Posn waypoint : hallway.getWaypoints()) {
        if (maxCol < waypoint.getCol()) {
          maxCol = waypoint.getCol();
        }
      }
    }
    for (Room room : rooms) {
      int maxRoomCol = room.getUpperLeft().getCol() + room.getCols();
      if (maxCol < maxRoomCol) {
        maxCol = maxRoomCol;
      }
    }
    return maxCol + 5;
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
    this.levelRows = 101;
    this.levelCols = 101;
    this.levelGrid = new String[this.levelRows][this.levelCols];
    this.initGridSpace();
    this.tileGrid = new Tile[this.levelRows][this.levelCols];
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
    this.rand = new Random();

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
    for (int row = 0; row < this.levelRows; row++) {
      for (int col = 0; col < this.levelCols; col++) {
        Tile tile = new Tile(true);
        tile.setNothing(true);
        tile.setPosition(new Posn(row, col));
        tileGrid[row][col] = tile;
      }
    }
  }

  /**
   * Populates the entire grid of the level with an initial value of "  "
   */
  private void initGridSpace() {
    for (int i = 0; i < this.levelRows; i++) {
      for (int j = 0; j < this.levelCols; j++) {
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

    int row = levelExitPosn.getRow();
    int col = levelExitPosn.getCol();

    tileGrid[row][col].setDoor(levelExit);
    levelGrid[row][col] = levelExit.toString();
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
    int row = keyPosition.getRow();
    int col = keyPosition.getCol();
    Posn exitKeyPosition = new Posn(row, col);
    this.exitKeyPosition = exitKeyPosition;
    ExitKey exitKey = new ExitKey(exitKeyPosition);
    tileGrid[row][col].setCollectable(exitKey);
    levelGrid[exitKeyPosition.getRow()][exitKeyPosition.getCol()] = exitKey.toString();
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
   * Spawns players and adversaries in the level randomly on valid placements. Valid placements (any
   * traversable room tile, not already occupied by a player or an adversary and not containing a
   * key or exit).
   *
   * @param players     the list of players to be placed
   * @param adversaries the list of adversaries to be places
   */
  public void spawnActorsRandomly(List<Actor> players, List<Actor> adversaries) {
    List<Room> copyRooms = new ArrayList<>(this.rooms);
    this.removeExistingOccupiers();
    if (copyRooms.size() == 1) {
      Room room = copyRooms.get(0);
      // place actors on traversable tiles in a random room
      this.placeActorsInRoomRand(room, players);
      // place adversaries on traversable tiles in the same room
      this.placeActorsInRoomRand(room, adversaries);
    } else {
      Room room1 = copyRooms.get(this.rand.nextInt(copyRooms.size()));
      copyRooms.remove(room1);
      Room room2 = copyRooms.get(this.rand.nextInt(copyRooms.size()));
      // place actors on traversable tiles in a random room
      this.placeActorsInRoomRand(room1, players);
      // place adversaries on traversable tiles a seperate random room
      this.placeActorsInRoomRand(room2, adversaries);
    }
  }

  /**
   * Remove all the occupiers in a level. This will get rid of any lurkers
   * from previous play throughs of the game.
   */
  private void removeExistingOccupiers() {
    for (int i = 0; i < this.levelRows; i++) {
      for (int j = 0; j < this.levelCols; j++) {
        this.tileGrid[i][j].setOccupier(null);
        this.levelGrid[i][j] = this.tileGrid[i][j].toString();
      }
    }
  }

  /**
   * Randomly places the given list of actors in the given room.
   *
   * @param room   the room in which the actors should be placed
   * @param actors the actors
   */
  private void placeActorsInRoomRand(Room room, List<Actor> actors) {
    Posn levelPosition;
    for (Actor actor : actors) {
      levelPosition = room.generateRandomUnoccupiedTile();
      actor.setPosition(levelPosition);
      tileGrid[levelPosition.getRow()][levelPosition.getCol()].setOccupier(actor);
      levelGrid[levelPosition.getRow()][levelPosition.getCol()] = actor.representation();
    }
  }

  /**
   * Helper method that places the given actors in traversable tiles in the given room. Players are
   * placed starting in the upper left corner.
   *
   * @param room   the given room the actors should be places
   * @param actors the given actors that need to be placed
   */
  private void placeActorsInRoom(Room room, List<Actor> actors) {
    Posn levelPosition;
    for (Actor actor : actors) {
      levelPosition = firstUnoccupiedTilePosition(room);
      actor.setPosition(levelPosition);
      tileGrid[levelPosition.getRow()][levelPosition.getCol()].setOccupier(actor);
      levelGrid[levelPosition.getRow()][levelPosition.getCol()] = actor.representation();
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
    for (int roomX = 0; roomX < room.getRows(); roomX++) {
      for (int roomY = 0; roomY < room.getCols(); roomY++) {
        String tileString = levelGrid[roomX + upperLeft.getRow()][roomY + upperLeft.getCol()];
        if (tileString.equals(".")) {
          levelPosition = new Posn(roomX + upperLeft.getRow(), roomY + upperLeft.getCol());
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
      int row = posn.getRow();
      int col = posn.getCol();
      tileGrid[row][col].setOccupier(actor);
      levelGrid[row][col] = actor.representation();
      actor.setPosition(posn);
    }
  }

  /**
   * Removes the key from this level.
   */
  public void removeKey() {
    Posn exitKeyPos = this.exitKeyPosition;
    int row = exitKeyPos.getRow();
    int col = exitKeyPos.getCol();

    this.tileGrid[row][col].setCollectable(null);
    this.levelGrid[row][col] = this.tileGrid[row][col].toString();
  }

  /**
   * Expels the given player from the level
   *
   * @param oldPosition is the old position the player to be expelled
   */
  public void expelPlayer(Posn oldPosition) {
    int row = oldPosition.getRow();
    int col = oldPosition.getCol();

    this.tileGrid[row][col].setOccupier(null);
    this.levelGrid[row][col] = this.tileGrid[row][col].toString();
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
    int prevRow = prevPosition.getRow();
    int prevCol = prevPosition.getCol();
    // remove player from old position
    tileGrid[prevRow][prevCol].setOccupier(null);
    levelGrid[prevRow][prevCol] = tileGrid[prevRow][prevCol].toString();

    int newRow = newPosition.getRow();
    int newCol = newPosition.getCol();
    Tile newTile = tileGrid[newRow][newCol];
    // add player to new position if there no current occupier
    if (newTile.getOccupier() == null) {
      newTile.setOccupier(p);
      levelGrid[newRow][newCol] = p.representation();
    }
    return newTile;
  }

  /**
   * This method updates the level object to reflect a players move. This method assumes the move
   * has already been deemed valid by the Rule Component.
   *
   * @param p           is the player that has moved
   * @param newPosition is the new position of the move
   * @return if the player has landed on exit or not
   */
  public Tile handleAdversaryMove(Actor p, Posn newPosition) {
    Posn prevPosition = p.getPosition();
    int prevRow = prevPosition.getRow();
    int prevCol = prevPosition.getCol();
    // remove player from old position
    tileGrid[prevRow][prevCol].setOccupier(null);
    levelGrid[prevRow][prevCol] = tileGrid[prevRow][prevCol].toString();

    int newRow = newPosition.getRow();
    int newCol = newPosition.getCol();
    Tile newTile = tileGrid[newRow][newCol];
    // add player to new position if there no current occupier
    if (newTile.getOccupier() == null) {
      newTile.setOccupier(p);
      levelGrid[newRow][newCol] = newTile.toString();
      p.setPosition(newPosition);
    }
    return newTile;
  }


  /**
   * Creates the ASCII string representation of a GameObjects.Level and all its data. Assumes the
   * grid has already be initialized.
   */
  public String createLevelString() {
    String levelAcc = "";
    for (int row = 0; row < levelRows; row++) {
      for (int col = 0; col < levelCols; col++) {
        levelAcc += this.levelGrid[row][col];
      }
      if (row != this.levelGrid.length - 1) {
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
      for (int x = 0; x < room.getRows(); x++) {
        for (int y = 0; y < room.getCols(); y++) {
          this.levelGrid[x + upperLeft.getRow()][y + upperLeft.getCol()] = roomGrid[x][y];
          this.tileGrid[x + upperLeft.getRow()][y + upperLeft.getCol()] = roomTileGrid[x][y];
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
      int row = posn.getRow();
      int col = posn.getCol();

      Tile wayPointTile = new Tile(false);
      wayPointTile.setPosition(new Posn(row, col));
      wayPointTile.setWayPoint(true);
      this.tileGrid[row][col] = wayPointTile;
      this.levelGrid[row][col] = wayPointTile.toString();

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
      if (start.getRow() == end.getRow()) {
        // check if tiles should be laid up or down
        int direction = calcDirection(start.getCol(), end.getCol());
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(t);
          tile.setPosition(new Posn(start.getRow(), start.getCol() + ((t + 1) * direction)));
          this.levelGrid[start.getRow()][start.getCol() + ((t + 1) * direction)] = tile.toString();
          this.tileGrid[start.getRow()][start.getCol() + ((t + 1) * direction)] = tile;
        }
      } else if (start.getCol() == end.getCol()) {
        // check if tiles should be laid right or left
        int direction = calcDirection(start.getRow(), end.getRow());
        for (int t = 0; t < tileSegement.size(); t++) {
          Tile tile = tileSegement.get(t);
          tile.setPosition(new Posn(start.getRow() + ((t + 1) * direction), start.getCol()));
          // start = 0, t = 0, direction = -1
          this.levelGrid[start.getRow() + ((t + 1) * direction)][start.getCol()] = tile.toString();
          this.tileGrid[start.getRow() + ((t + 1) * direction)][start.getCol()] = tile;
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
   * Check whether the given point is traversable (door or "floor" or in a hallway)
   *
   * @param point the given posn
   * @return if the point is traversable or not
   */
  public boolean checkTraversable(Posn point) {
    int row = point.getRow();
    int col = point.getCol();
    if (this.levelCols <= col || this.levelRows <= row) {
      return false;
    }
    if (this.tileGrid[row][col] == null) {
      return false;
    }
    Tile tile = this.tileGrid[row][col];
    return !tile.isWall() && !tile.isNothing();
  }

  /**
   * Returns the type of object occupying the tile at the given point.
   *
   * @param point the posn to check for an object
   * @return either "key", "door" or "null"
   */
  public String checkObjectType(Posn point) {
    Tile tile = tileGrid[point.getRow()][point.getCol()];
    String tileRep = levelGrid[point.getRow()][point.getCol()];
    if (tileRep.equals("|")) {
      return tile.getDoor().isLevelExit() ? "exit" : "null";
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
   * This method returns true if the destination Posn is reachable from the given current position
   * by two cardinal moves. The destination must also be traversable
   *
   * @param currentPosition the players current position
   * @param destination     the destination
   * @return
   */
  public boolean canActorMoveLevel(Posn currentPosition, Posn destination) {
    List<Posn> firstPossibleMoves = getCardinalMoves(currentPosition);
    List<Posn> allPossibleMoves = new ArrayList(firstPossibleMoves);
    for (Posn move : firstPossibleMoves) {
      allPossibleMoves.addAll(getCardinalMoves(move));
    }
    return currentPosition.equals(destination) ||
            (checkTraversable(destination) && allPossibleMoves.contains(destination));
  }

  /**
   * Returns the list of Posns referencing the NORTH EAST SOUTH WEST positions relative to the given
   * position makes sure that all the moves are within bounds
   * Restricts moves to void spaces
   *
   * @param position
   * @return
   */
  public List<Posn> getCardinalMoves(Posn position) {
    List<Posn> possiblePosns = new ArrayList<>();

    int playerRow = position.getRow();
    int playerCol = position.getCol();

    // find the north tile
    if ((playerRow - 1) >= 0) {
      int newRow = playerRow - 1;
      Tile tile = tileGrid[newRow][playerCol];
      if (!tile.isNothing()) {
        possiblePosns.add(new Posn(playerRow - 1, playerCol));
      }
    }
    // find the west tile
    if ((playerCol - 1) >= 0) {
      int newCol = playerCol - 1;
      Tile tile = tileGrid[playerRow][newCol];
      if (!tile.isNothing()) {
        possiblePosns.add(new Posn(playerRow, newCol));
      }
    }
    // find the east tile
    if ((playerCol + 1) < this.tileGrid[0].length) {
      int newCol = playerCol + 1;
      Tile tile = tileGrid[playerRow][newCol];
      if (!tile.isNothing()) {
        possiblePosns.add(new Posn(playerRow, newCol));
      }
    }
      // find the south tile
      if ((playerRow + 1) < this.tileGrid.length) {
        int newRow = playerRow + 1;
        Tile tile = tileGrid[newRow][playerCol];
        if (!tile.isNothing()) {
          possiblePosns.add(new Posn(newRow, playerCol));
        }
      }
      return possiblePosns;
    }


    /**
     * This method returns a 5x5 grid of tiles where the given position is the center if any of the
     * surrounding positions are not tiles (ie. its on the edge of a room or level) those spaces are
     * set to null
     *
     * @param position the center position
     * @return a 5x5 grid with the given position as the center
     */
    public List<List<Tile>> getSurroundingsForPosn (Posn position){
      int row = position.getRow();
      int col = position.getCol();

      int surroundingRow = row - 2;
      int surroundingCol = col - 2;
      List<List<Tile>> surroundingsGrid = new ArrayList<>();

      for (int i = 0; i < 5; i++) {
        List<Tile> surroundings = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
          // make sure that the tile is in the grid, if not add null
          if (surroundingRow + i < this.tileGrid.length && surroundingRow + i >= 0 && surroundingCol + j < this.tileGrid[0].length && surroundingCol + j >= 0) {
            surroundings.add(this.tileGrid[surroundingRow + i][surroundingCol + j]);
          } else {
            surroundings.add(null);
          }
        }
        surroundingsGrid.add(surroundings);
      }
      return surroundingsGrid;
    }

    /**
     * Removes any occupiers on the exit door
     */
    public void clearExitDoor () {
      this.tileGrid[exitDoorPosition.getRow()][exitDoorPosition.getCol()].setOccupier(null);
    }

    /**
     * Generate a random posn in another random room that is different from the current position. If
     * no room has places, choose from the current position's room.
     *
     * @param currentPosition
     */
    public Posn generateTransportPosition (Posn currentPosition){
      List<Room> roomsCopy = new ArrayList<>(this.getRooms());
      Collections.shuffle(roomsCopy);
      Room currentRoom = this.getRoomOfPosn(currentPosition);
      roomsCopy.remove(currentRoom);
      // for every room, if posn not in room, transport there
      for (Room room : roomsCopy) {
        if (room.checkForValidSpace()) {
          return room.getRandomValidSpace();
        }
      }

      if (currentRoom.checkForValidSpace()) {
        return currentRoom.getRandomValidSpace();
      } else {
        return currentPosition;
      }
    }

    /**
     * Returns the room that a given position is in
     *
     * @param currentPosition the given position
     * @return
     */
    private Room getRoomOfPosn (Posn currentPosition){
      for (Room room : this.rooms) {
        if (room.isPosnInRoom(currentPosition)) {
          return room;
        }
      }
      return null;
    }

    public List<Room> getRooms () {
      return rooms;
    }

    public void setRooms (List < Room > rooms) {
      this.rooms = rooms;
    }

    public List<Hallway> getHallways () {
      return hallways;
    }

    public void setHallways (List < Hallway > hallways) {
      this.hallways = hallways;
    }

    public int getLevelRows () {
      return levelRows;
    }

    public void setLevelRows ( int levelRows){
      this.levelRows = levelRows;
    }

    public int getLevelCols () {
      return levelCols;
    }

    public void setLevelCols ( int levelCols){
      this.levelCols = levelCols;
    }

    public String[][] getLevelGrid () {
      return levelGrid;
    }

    public void setLevelGrid (String[][]levelGrid){
      this.levelGrid = levelGrid;
    }

    public Tile[][] getTileGrid () {
      return tileGrid;
    }

    public void setTileGrid (Tile[][]tileGrid){
      this.tileGrid = tileGrid;
    }

    public Posn getExitKeyPosition () {
      return exitKeyPosition;
    }

    public void setExitKeyPosition (Posn exitKeyPosition){
      this.exitKeyPosition = exitKeyPosition;
    }

    public Posn getExitDoorPosition () {
      return exitDoorPosition;
    }

    public void setExitDoorPosition (Posn exitDoorPosition){
      this.exitDoorPosition = exitDoorPosition;
    }

    public Random getRand () {
      return rand;
    }

    public void setRand (Random rand){
      this.rand = rand;
    }


  }
