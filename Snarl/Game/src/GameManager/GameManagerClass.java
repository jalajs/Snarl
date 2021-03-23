package GameManager;

import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

import Action.Action;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import Action.MoveAction;
import GameObjects.Tile;
import GameState.GameState;
import GameState.GameStateModel;
import Observer.Subject;
import RuleChecker.RuleChecker;
import RuleChecker.RuleCheckerClass;
import User.User;
import User.LocalUser;

/**
 * Represents the GameManger tasked with sequencing the game
 */
public class GameManagerClass implements GameManager {
  private GameState gs;
  private List<User> users;
  private int turn;
  private List<Adversary> adversaries;
  private final RuleChecker ruleChecker = new RuleCheckerClass();
  private Subject subject;
  private ArrayList<ArrayList<Posn>> moveInput;

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
      User user = new LocalUser(name);
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
  public void addAdversary(String type, String name) {
    Adversary adversary = new Adversary(type, name);
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
   * Starts the game trace for the testing task.
   *
   * @param level the level to start the game with
   * @param posns the list of intital positions for actors.
   *              The first n positions are for the users and any subsequent posn is for an Adversary we create
   */
  @Override
  public void startGameTrace(Level level, List<Posn> posns) {
    GameState gameState = new GameStateModel(level);
    this.gs = gameState;

    List<Actor> players = new ArrayList<>();
    List<Actor> actorAdversaries = new ArrayList<>();

    for (int i = 0; i < users.size(); i ++) {
      Actor player = new Player(users.get(i).getName());
      users.get(i).setCurrentPosition(posns.get(i));
      player.setPosition(posns.get(i));
      players.add(player);
    }
    for(int i = users.size(); i < posns.size(); i ++) {
      Adversary adversary = new Adversary("Zombie", String.valueOf(i));
      adversary.setPosition(posns.get(i));
      actorAdversaries.add(adversary);
    }
    gameState.initLevelGrid();
    gameState.initGameStateWhereActorsHavePositions(players, actorAdversaries, level.getExitKeyPosition());

    // give each User an initial surroundings
    // use the gameState
    for (int i = 0; i < users.size(); i ++) {
      List<List<Tile>> surroundings = gameState.getSurroundingsForPosn(players.get(i).getPosition());
      users.get(i).setSurroundings(surroundings);
    }
  }





  /**
   * This method contacts every user via their update method and provides them with the newest
   * relevant information, namely their new visible tiles and whether or not the level is exitable.
   */
  @Override
  public void updateUsers() {
    // to do: get real position
    for (User user : this.users) {
      user.update(this.gs.calculateVisibleTilesForUser(user.getCurrentPosition()), this.gs.isExitable(), user.getCurrentPosition());
    }
  }

  /**
   * This method prompts the next player to take their turn. It then executes the action returned
   * from the turn. This method is expected to run continously throughout the levels duration.
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

  /**
   * Gets the user with the given string
   * @param name the given string
   * @return the User with the corresponding name. Returns null if no such user exists.
   */
  @Override
  public User getUserByString(String name) {
    for (User user : this.users) {
        if (user.getName().equals(name)) {
          return user;
        }
    }
    return null;
  }

  /**
   * Play out the move of the User's whose turn it is using the move input.
   * @return Return the trace of their move in a JSONArray
   */
  @Override
  public JSONArray playOutMove() {
    User user = this.users.get(turn);

  }


  @Override
  public List<String> getRemainingPlayers() {
    return null;
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


  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public ArrayList<ArrayList<Posn>> getMoveInput() {
    return moveInput;
  }

  /**
   * Set the move input stream to the given list of list of positions
   * @param actorMoveListList the positions represent the destination for each move
   */
  @Override
  public void setMoveInput(ArrayList<ArrayList<Posn>> actorMoveListList) {
    this.moveInput = actorMoveListList;
  }


}
