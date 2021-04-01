package GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Collectable;
import GameObjects.Level;
import Action.MoveAction;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Door;
import GameObjects.Tile;
import RuleChecker.RuleChecker;

public class GameStateModel implements GameState {
  private Level level;
  private boolean isExitable;
  private boolean playerIsOnExit;
  private List<Actor> actors;
  private List<Collectable> collectables;
  private List<Player> exitedPlayers;
  private int currentLevelNumber;
  private int totalLevels;

  public GameStateModel(Level level) {
    this.level = level;
    this.isExitable = false;
    this.playerIsOnExit = false;
    this.actors = new ArrayList<>();
    this.collectables = new ArrayList<>();
    this.exitedPlayers = new ArrayList<>();
  }

  /**
   * Creates the initial game state by placing the given actors in the game. Players are placed in
   * the first room, and adversaries are placed in the last room. The key is placed on the given
   * position
   *
   * @param players     are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn     is the position to place the key
   */
  public void initGameState(List<Actor> players, List<Actor> adversaries, Posn keyPosn) {
    this.level.initGrid();
    // place the player in the top-left most room and the adversaries in the bottom right-most room
    this.level.spawnActors(players, adversaries);
    // place the key somewhere in the level
    this.level.dropKey(keyPosn);
    // set the this.actors to the list of players and adversaries
    for (Actor player : players) {
      this.actors.add(player);
    }
    for (Actor adversary : adversaries) {
      this.actors.add(adversary);
    }
  }

  /**
   * Initializes the game state such adversaries and players are dropped randomly on to
   * valid tiles throughout the whole level.
   *
   * @param players     are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn     is the position to place the key
   * @return a map associating the player's names to their position
   */
  public Map<String, Posn> initLocalGameState(List<Actor> players, List<Actor> adversaries, Posn keyPosn) {
    //this.level.initGrid();
    this.level.dropKey(keyPosn);
    // place the player and adversaries randomly
    this.level.spawnActorsRandomly(players, adversaries);

    Map<String, Posn> nameToPosnMap = new HashMap<String, Posn>();


    // place the key somewhere in the level
    // set the this.actors to the list of players and adversaries
    for (Actor player : players) {
      this.actors.add(player);
      nameToPosnMap.put(player.getName(), player.getPosition());
    }
    for (Actor adversary : adversaries) {
      this.actors.add(adversary);
    }

    return nameToPosnMap;
  }

  /**
   * Creates the initial game state by placing the given actors in the game. Players and adversaries
   * are placed where their positions are. The key is placed on the given position.
   * <p>
   * This is used testState
   *
   * @param players     are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn     is the position to place the key
   */
  public void initGameStateWhereActorsHavePositions(List<Actor> players, List<Actor> adversaries, Posn keyPosn) {
    // places all the actors based on their own positions
    this.level.placeActorsInLevel(players);
    this.level.placeActorsInLevel(adversaries);
    // place the key somewhere in the level
    if (keyPosn != null) {
      this.level.dropKey(keyPosn);
    }
    // set the this.actors to the list of players and adversaries
    for (Actor player : players) {
      this.actors.add(player);
    }
    for (Actor adversary : adversaries) {
      this.actors.add(adversary);
    }
  }

  /**
   * This method simply initializes the level grid. We created it so that we can initialize the
   * level grid at the GS level from the test harnesses.
   */
  public void initLevelGrid() {
    this.level.initGrid();
  }

  /**
   * Creates an intermediate game state. This method places the given players and adversaries in
   * this.level.
   *
   * @param playerLocations    given player locations in the game state
   * @param adversaryLocations given adversary locations in the game state
   * @param givenIsExitable    status of the level exit
   */
  public void intermediateGameState(List<Posn> playerLocations,
                                    List<Posn> adversaryLocations,
                                    boolean givenIsExitable) {
    // place all the players and adversaries in the actor map
    for (Posn posn : playerLocations) {
      Player newPlayer = new Player();
      newPlayer.setPosition(posn);
      this.actors.add(newPlayer);
    }
    for (Posn posn : adversaryLocations) {
      Adversary newAdversary = new Adversary();
      newAdversary.setPosition(posn);
      this.actors.add(newAdversary);
    }
    // set isExitable to the given
    this.isExitable = givenIsExitable;
    // use the actorPositions to populate the level
    this.level.placeActorsInLevel(this.actors);
  }

  /**
   * Modifies the game state after a player/adversary collects exit key
   */
  public void handleKeyCollection() {
    this.isExitable = true;
    this.level.removeKey();
  }

  /**
   * This method returns a 5x5 grid of tiles where the given position is the center if any of the
   * surrounding positions are not tiles (ie. its on the edge of a room or level) those spaces are
   * set to null
   *
   * @param position the center position
   * @return a 5x5 grid with the given position as the center
   */
  @Override
  public List<List<Tile>> getSurroundingsForPosn(Posn position) {
    return this.level.getSurroundingsForPosn(position);
  }

  /**
   * Modifies the game state after a player is expelled
   *
   * @param expelledPlayer
   * @param oldPosition    the position the player/adversary moves from
   */
  public void handlePlayerExpulsion(Player expelledPlayer, Posn oldPosition) {
    this.exitedPlayers.add(expelledPlayer);
    this.actors.remove(expelledPlayer);
    this.level.expelPlayer(oldPosition);
  }


  /**
   * Modifies the game state after a player is expelled
   *
   * @param exitedPlayer
   * @param oldPosition  the position the player/adversary moves from
   */
  public void handlePlayerExit(Player exitedPlayer, Posn oldPosition) {
    this.exitedPlayers.add(exitedPlayer);
    this.actors.remove(exitedPlayer);
    this.level.expelPlayer(oldPosition);
    this.level.clearExitDoor();
  }


  /**
   * Modifies the game state to reflect a player move. This method assumed the move has already been
   * validated by the Rule Component.
   *
   * @param p    refers to player who has just moved
   * @param posn is the new position of the
   */
  public String handleMovePlayer(Player p, Posn posn) {
    Tile dest = this.level.handlePlayerMove(p, posn);
    if (dest.getDoor() != null) {
      Door door = dest.getDoor();
      this.playerIsOnExit = door.isLevelExit();
    }
    // set the player's position to the new posn
    p.setPosition(posn);
    return dest.getInteraction(p);
  }

  /**
   * Renders the current game state
   *
   * @return String that represents the GameState.GameState
   */
  public String renderGameState() {
    this.level.initGrid();
    return this.level.createLevelString();
  }

  public Level getLevel() {
    return level;
  }

  @Override
  public void setLevel(Level newLevel) {
    this.level = level;
  }

  @Override
  public boolean validMove(Actor actor, Posn destination) {
    return false;
  }

  @Override
  public boolean isGameComplete() {
    return this.currentLevelNumber == this.totalLevels && this.isLevelEnd();
  }

  @Override
  public void updateGame() {

  }

  @Override
  public void endGame() {

  }


  /**
   * A level is over if the exit door is unlocked and a player goes through it or if all players in
   * the level are expelled.
   *
   * @return whether or not the given Level is over or not
   */
  public boolean isLevelEnd() {
    if (exitedPlayers.size() == this.numberOfPlayers()) {
      return true;
    }
    return this.isExitable && this.playerIsOnExit;
  }


  /**
   * A GameState is invalid if any actors are on walls or otherwise in bad positions or the exit key
   * is in a bad position
   *
   * @return true if the GameState is valid
   */
  @Override
  public boolean isStateValid() {
    for (Actor actor : this.actors) {
      // check if each actor is on a non wall tile
      Posn actorPosition = actor.getPosition();
      if (!level.checkTraversable(actorPosition)) {
        return false;
      }
    }
    return level.checkTraversable(level.getExitKeyPosition());
  }

  /**
   * Calculates the number of players in the list of actors
   *
   * @return the number of players
   */
  private int numberOfPlayers() {
    int numberOfPlayers = 0;
    for (Actor actor : actors) {
      if (actor.isPlayer()) {
        numberOfPlayers++;
      }
    }
    return numberOfPlayers;
  }

  /**
   * This method handles the MoveAction object. This involves validating the move and interaction
   * and then performing the move and interaction
   *
   * @param action  the given MoveAction object, containing the players current position, the
   *                interation type, and the destination
   * @param checker the given RuleChecker
   * @return boolean returns true if the move was executed
   */
  @Override
  public boolean handleMoveAction(MoveAction action, RuleChecker checker) {
    Posn destination = action.getDestination();
    Posn currentPosition = action.getCurrentPosition();
    Player player = this.findPlayerByPosition(currentPosition);
    if (checker.isMoveValid(this.level, player, destination)) {
      if (checker.isInteractionValid(this.isExitable, player,
              this.level.getTileGrid()[destination.getRow()][destination.getCol()])) {
        // move the player to the new tile
        String interactionType = this.handleMovePlayer(player, destination);
        action.setInteractionType(interactionType);
        // perform the prescribed interaction
        handleInteractionType(interactionType, player, currentPosition, action);
        return true;
      }
    }
    action.setInteractionType("Invalid");
    return false;
  }

  /**
   * This method handles the interactionType and performs the appropriate mutation
   *
   * @param interactionType
   * @param player
   * @param currentPosition
   * @param action
   */
  private void handleInteractionType(String interactionType, Player player, Posn currentPosition, MoveAction action) {
    switch (interactionType) {
      case "Exit":
        if (this.isExitable()) {
          this.handlePlayerExit(player, currentPosition);
        } else {
          action.setInteractionType("OK");
        }
        break;
      case "Key":
        this.handleKeyCollection();
        break;
      case "Eject":
        this.handlePlayerExpulsion(player, currentPosition);
        break;
      case "OK":
        break;
    }
  }

  /**
   * This method finds the player who is on the given position
   *
   * @param position the position of a Player (never an adversary)
   * @return the player on the position
   */
  private Player findPlayerByPosition(Posn position) {
    Tile[][] levelGrid = this.level.getTileGrid();
    Tile tile = levelGrid[position.getRow()][position.getCol()];
    Actor tileActor = tile.getOccupier();
    if (tileActor != null) {
      return (Player) tileActor;
    }
    return null;
  }

  /**
   * This method calculate the tiles visible from the given posn. This method is currently just a
   * STUB and will be implemented when relevant to a future milestone.
   *
   * @param userPosn the current position of the user
   * @return
   */
  public List<List<Tile>> calculateVisibleTilesForUser(Posn userPosn) {
    List<List<Tile>> visibleTiles = new ArrayList<>();

    return visibleTiles;
  }

  public List<Adversary> getAdversaries() {
    List<Adversary> adversaries = new ArrayList<>();
    for (Actor actor : this.actors) {
      if (!actor.isPlayer()) {
        adversaries.add((Adversary) actor);
      }
    }
    return adversaries;
  }

  public boolean isExitable() {
    return isExitable;
  }

  public void setExitable(boolean exitable) {
    isExitable = exitable;
  }

  public List<Actor> getActors() {
    return actors;
  }

  public void setActors(List<Actor> actor) {
    this.actors = actors;
  }

  public List<Collectable> getCollectables() {
    return collectables;
  }

  public void setCollectables(List<Collectable> collectables) {
    this.collectables = collectables;
  }

  public List<Player> getExitedPlayers() {
    return exitedPlayers;
  }

  public void setExitedPlayers(List<Player> exitedPlayers) {
    this.exitedPlayers = exitedPlayers;
  }

  public boolean isPlayerIsOnExit() {
    return playerIsOnExit;
  }

  public void setPlayerIsOnExit(boolean playerIsOnExit) {
    this.playerIsOnExit = playerIsOnExit;
  }

  public int getCurrentLevelNumber() {
    return currentLevelNumber;
  }

  public void setCurrentLevelNumber(int currentLevelNumber) {
    this.currentLevelNumber = currentLevelNumber;
  }

  public int getTotalLevels() {
    return totalLevels;
  }

  public void setTotalLevels(int totalLevels) {
    this.totalLevels = totalLevels;
  }
}
