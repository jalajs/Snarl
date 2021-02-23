import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStateModel implements GameState {
  private Level level;
  private boolean isExitable;
  private Map<Actor, Posn> actorPositions;
  private Map<Collectable, Posn> objectPositions;
  private List<Player> exitedPlayers;

  public GameStateModel(Level level) {
    this.level = level;
    this.isExitable = false; // actually do something
    this.actorPositions = new HashMap<>();
    this.objectPositions = new HashMap<>();
    this.exitedPlayers = new ArrayList<>();
  }

  /**
   * Creates the initial game state.
   * @param numPlayers Number of player to spawn in the initial game state.
   * @param numAdversaries number of adversaries to spawn in the initial game state
   */
  public void initGameState(int numPlayers, int numAdversaries, Posn keyPosn) {
    // place the player in the top-left most room in level
    // place the adversaries in the bottom right-most room
    this.actorPositions = this.level.spawnActors(numPlayers, numAdversaries);
    // place the key somewhere in the level
    // todo: this will need to be expanded to involve all objects for future milestone
    this.objectPositions = this.level.dropKey(keyPosn);
  }

  /**
   * Creates an intermediate game state. This method places the given players and adversaries in this.level.
   * @param playerLocations given player locations in the game state
   * @param adversaryLocations given adversary locations in the game state
   * @param givenIsExitable status of the level exit
   */
  public void intermediateGameState(List<Posn> playerLocations,
                                    List<Posn> adversaryLocations,
                                    boolean givenIsExitable) {
    // place all the players and adversaries in the actor map
    for (Posn posn : playerLocations){
      this.actorPositions.put(new Player(), posn);
    }
    for (Posn posn : adversaryLocations) {
      this.actorPositions.put(new Adversary(), posn);
    }
    // set isExitable to the given
    this.isExitable = givenIsExitable;
    // use the actorPositions to populate the level
    this.level.placeActorsInGivenLocations(this.actorPositions);
  }

  /**
   * Modifies the game state after a player/adversary collectsexit key
   */
  public void handleKeyCollection() {
      this.isExitable = true;
      this.level.removeKey();
  }

  /**
   * Modifies the game state after a player is expelled
   * @param expelledPlayer
   */
  public void handlePlayerExpulsion(Player expelledPlayer) {
    this.exitedPlayers.add(expelledPlayer);
    this.level.expelPlayer(expelledPlayer);
  }

  /**
   * Modifies the game state to reflect a player move. This method assumed the move has already been
   * validated by the Rule Component.
   * @param p refers to player who has just moved
   * @param posn is the new position of the
   */
  public void handleMovePlayer(Player p, Posn posn) {
    this.level.handlePlayerMove(p, posn);
    // set the player's position to the new posn
    p.setPosition(posn);
  }

  /**
   * Renders the current game state
   * @return String that represents the GameState
   */
  public String renderGameState() {
    return "";
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
    return false;
  }

  @Override
  public void movePlayer(Player player, Posn destination) {

  }

  @Override
  public void updateGame() {

  }

  @Override
  public void endGame() {

  }

  public Level getLevel() {
    return level;
  }

  public boolean isExitable() {
    return isExitable;
  }

  public void setExitable(boolean exitable) {
    isExitable = exitable;
  }

  public Map<Actor, Posn> getActorPositions() {
    return actorPositions;
  }

  public void setActorPositions(Map<Actor, Posn> actorPositions) {
    this.actorPositions = actorPositions;
  }

  public Map<Collectable, Posn> getObjectPositions() {
    return objectPositions;
  }

  public void setObjectPositions(Map<Collectable, Posn> objectPositions) {
    this.objectPositions = objectPositions;
  }

  public List<Player> getExitedPlayers() {
    return exitedPlayers;
  }

  public void setExitedPlayers(List<Player> exitedPlayers) {
    this.exitedPlayers = exitedPlayers;
  }
}
