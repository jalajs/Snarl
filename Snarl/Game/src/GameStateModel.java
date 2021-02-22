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


  @Override
  public void setLevel(Level newLevel) {

  }

  @Override
  public boolean validMove(Player player, Posn destination) {
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
