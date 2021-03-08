package GameState;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Collectable;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameState.GameState;

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
   * Creates the initial game state by placing the given actors in the game. Players
   * are placed in the first room, and adversaries are placed in the last room. The key is
   * placed on the given position
   *
   * @param players are the list of players in the game
   * @param adversaries are the adversaries in the game
   * @param keyPosn is the position to place the key
   */
  public void initGameState(List<Actor> players, List<Actor> adversaries, Posn keyPosn) {
    this.level.initGrid();
    // place the player in the top-left most room and the adversaries in the bottom right-most room
    this.level.spawnActors(players, adversaries);
    // place the key somewhere in the level
    this.level.dropKey(keyPosn);
    // set the this.actors to the list of players and adversaries
    for(Actor player: players) {
      this.actors.add(player);
    }
    for(Actor adversary: adversaries) {
      this.actors.add(adversary);
    }
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
    boolean reachExit = this.level.handlePlayerMove(p, posn);
    this.playerIsOnExit = reachExit;

    // set the player's position to the new posn
    p.setPosition(posn);
  }

  /**
   * Renders the current game state
   * @return String that represents the GameState.GameState
   */
  public String renderGameState() {
    this.level.initGrid();
    return this.level.createLevelString();
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
  public void movePlayer(Player player, Posn destination) {

  }

  @Override
  public void updateGame() {

  }

  @Override
  public void endGame() {

  }


  /**
   * A level is over if the exit door is unlocked and a player goes through it or
   *  if all players in the level are expelled.
   * @return whether or not the given Level is over or not
   */
  public boolean isLevelEnd() {
    if (exitedPlayers.size() == this.numberOfPlayers()) {
      return true;
    }
    return this.isExitable() && this.playerIsOnExit;
  }


  /**
   *  A GameState is invalid if any actors are on walls or otherwise in bad positions or the exit key
   *  is in a bad position
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
   * @return the number of players
   */
  private int numberOfPlayers() {
   int numberOfPlayers = 0;
   for (Actor actor: actors) {
     if (actor.isPlayer()) {
       numberOfPlayers++;
     }
   }
   return numberOfPlayers;
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
