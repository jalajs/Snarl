package GameManager;

import java.util.List;
import java.util.ArrayList;

import GameObjects.Action;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.MoveAction;
import GameState.GameState;
import GameState.GameStateModel;
import RuleChecker.RuleChecker;
import RuleChecker.RuleCheckerClass;
import User.User;
import User.UserClass;

/**
 * Represents the GameManger tasked with sequencing the game
 */
public class GameManagerClass implements GameManager {
  private GameState gs;
  private List<User> users;
  private int turn;
  private List<Adversary> adversaries;
  private final RuleChecker ruleChecker = new RuleCheckerClass();

  /**
   * This initialized the GameManager as a blank slate
   */
  public GameManagerClass() {
    this.gs = null;
    this.users = new ArrayList<>();
    this.turn = 0;
    this.adversaries = new ArrayList<>();
  }

  /**
   * Adds/registers a User by the given name to the list of Users Once we fully implement
   * registration, an error should be sent to any person who tries to register with an existing
   * name.
   *
   * @param name
   */
  @Override
  public void addPlayer(String name) {
    if (!doesNameAlreadyExist(name)) {
      User user = new UserClass(name);
      users.add(user);
    }
  }

  /**
   * Checks whether the given name already exists among the current users
   *
   * @param name the given name
   * @return whether or no the name already exists
   */
  private boolean doesNameAlreadyExist(String name) {
    for (User user : this.users) {
      if (name.equals(user.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds/registers an arbitrary number of adversaries.
   *
   * @param type The string type refers to whether the Adversary is an internal component, a plug-in
   *             loaded from somewhere else, or an AI connected via a network
   */
  @Override
  public void addAdversary(String type) {
    Adversary adversary = new Adversary(type);
    adversaries.add(adversary);
  }

  /**
   * This method starts the game by initializing the GameState. This entails setting the level and
   * creating Player objects to represent each User object.
   *
   * @param level the level to start the game with
   */
  @Override
  public void startGame(Level level) {
    GameState gameState = new GameStateModel(level);
    List<Actor> players = new ArrayList<>();
    List<Actor> actorAdversaries = new ArrayList<>();

    for (User user : this.users) {
      Actor player = new Player(user.getName());
      players.add(player);
    }
    for (Adversary adversary : this.adversaries) {
      actorAdversaries.add(adversary);
    }

    gameState.initGameState(players, actorAdversaries, level.getExitKeyPosition());

    // run game
    // note, this currently only goes to the completion of a single level
    while (ruleChecker.isLevelEnd(this.gs)) {
      this.promptPlayerTurn();
    }
  }

  /**
   * This method contacts every user via their update method and provides them with the
   * newest relevant information, namely their new visible tiles and whether or not the level is
   * exitable.
   *
   */
  @Override
  public void updateUsers() {
    for (User user : this.users) {
      user.update(this.gs.calculateVisibleTilesForUser(user.getCurrentPosition()), this.gs.isExitable());
    }
  }

  /**
   *  This method prompts the next player to take their turn. It then executes the action returned
   *  from the turn. This method is expected to run continously throughout the levels duration.
   *
   */
  @Override
  public void promptPlayerTurn() {
    // check if it is the user's turn
    if (turn < this.users.size()) {
      // ask for input again if invalid
      Action action = users.get(turn).turn();
      this.executeAction("player", action);
    }
    // check if it is an adversaries turn
    if (this.turn > this.users.size()) {
      Action action = adversaries.get(turn).turn();
      this.executeAction("adversary", action);
    }
    this.updateTurn();
    this.updateUsers();
  }

  /**
   * Called after each turn, increased the turn count or sets it to zero
   */
  public void updateTurn() {
    if (turn == this.users.size() + this.adversaries.size() - 1) {
      this.turn = 0;
    } else {
      this.turn++;
    }
  }

  /**
   * This method exectutes the given action on the game state.
   *
   * @param actorType indicates the type of actor making the move. This is necessary to
   *                  differentiate between adversaries and players. The adversary portion remains
   *                  empty until further milestones.
   * @param action
   */
  @Override
  public void executeAction(String actorType, Action action) {
    if (actorType.equals("player")) {
      String actionType = action.getType();
      if (actionType.equals("move")) {
        MoveAction moveAction = (MoveAction) action;
        this.gs.handleMoveAction(moveAction, ruleChecker);
      }
    } else if (actorType.equals("adversary")) {
      // todo: implement adversary interaction with more information at a future milestone
    }
  }

  public GameState getGs() {
    return gs;
  }

  public void setGs(GameState gs) {
    this.gs = gs;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public List<Adversary> getAdversaries() {
    return adversaries;
  }

  public void setAdversaries(List<Adversary> adversaries) {
    this.adversaries = adversaries;
  }

  public RuleChecker getRuleChecker() {
    return ruleChecker;
  }
}
