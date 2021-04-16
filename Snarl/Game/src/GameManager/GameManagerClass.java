package GameManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import Action.Action;
import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import Action.MoveAction;
import GameState.GameState;
import GameState.GameStateModel;
import Observer.Subject;
import RuleChecker.RuleChecker;
import RuleChecker.RuleCheckerClass;
import User.User;
import User.LocalUser;
import Adversary.Zombie;
import Adversary.Ghost;
import Adversary.SnarlAdversary;

/**
 * Represents the GameManger tasked with sequencing the game
 */
public class GameManagerClass implements GameManager {
  private GameState gs;
  private List<User> users;
  private int turn; // this references who's turn it is in
  private String nextUser;
  private List<SnarlAdversary> adversaries;
  private final RuleChecker ruleChecker = new RuleCheckerClass();
  private Subject subject;
  private ArrayList<ArrayList<Posn>> moveInput;
  private List<Level> levels;
  private int startLevel;

  /**
   * This initialized the GameManager as a blank slate
   */
  public GameManagerClass() {
    this.gs = null;
    this.users = new ArrayList<>();
    this.turn = 0;
    this.adversaries = new ArrayList<>();
    this.nextUser = "";
    this.startLevel = 0;
    this.levels = new ArrayList<>();
  }

  /**
   * This creates a GameManagerClass object with a list of levels and the current level. It is used
   * by localSnarl.
   *
   * @param levels     List of levels
   * @param startLevel the starting level
   */
  public GameManagerClass(List<Level> levels, int startLevel) {
    this.gs = null;
    this.users = new ArrayList<>();
    this.turn = 0;
    this.adversaries = new ArrayList<>();
    this.nextUser = "";
    this.startLevel = startLevel;
    this.levels = levels;
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
   * Adds a remote user to the gameManager. assumes name validation has already happened
   * @param user
   */
  public void addRemotePlayer(User user) {
    users.add(user);
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
    if (type.equals("zombie")) {
      SnarlAdversary adversary = new Zombie(name);
      adversaries.add(adversary);
    } else if (type.equals("ghost")) {
      SnarlAdversary adversary = new Ghost(name);
      adversaries.add(adversary);
    }
  }

  /**
   * This is the highest level method that runs the local game
   */
  @Override
  public void runLocalGame(boolean observeValue) {
    String gameCondition = "";
    int levelNumber = startLevel;
    // start each level and let it play through
    for (int i = startLevel - 1; i < levels.size(); i++) {
      Level currentLevel = levels.get(i);
      gameCondition = startLocalLevel(currentLevel, levelNumber, observeValue);
      // if the game is over, break out of the level advancement
      if (gameCondition.equals("Win") || gameCondition.equals("Loss")) {
        break;
      }
      levelNumber++;
      turn = 0;
    }
    this.printEndResult(gameCondition, levelNumber);
  }

  @Override
  public void runRemoteGame(boolean observeValue) {
    System.out.println("Remote game is running");
    String gameCondition = "";
    int levelNumber = startLevel;
    // start each level and let it play through
    for (int i = startLevel - 1; i < levels.size(); i++) {
      Level currentLevel = levels.get(i);
      // send start-level json
      this.notifyAllUsersLevelStart(i);
      gameCondition = startLocalLevel(currentLevel, levelNumber, observeValue);
      // if the game is over, break out of the level advancement
      this.updateAllUserStats();
      System.out.println("Game condition: " + gameCondition);
      if (gameCondition.equals("Win") || gameCondition.equals("Loss")) {
        break;
      }
      this.notifyAllUsersLevelEnd();
      levelNumber++;
      turn = 0;
    }
    this.notifyAllUsersGameEnd();
  }


  /**
   * Updates the stats for each user at the end of a level
   */
  private void updateAllUserStats() {
    List<String> exitedPlayers = this.getNameListFromPlayers(gs.getExitedPlayers());
    List<String> expelledPlayers = this.getNameListFromPlayers(gs.getEjectedPlayers());
    boolean wasTheKeyFound = this.gs.getKeyFinder() != null;
    for (User user: this.users) {
      boolean isExited = exitedPlayers.contains(user.getName());
      boolean isEjected = expelledPlayers.contains(user.getName());
      boolean isKeyFinder = wasTheKeyFound && this.gs.getKeyFinder().getName().equals(user.getName());
      user.updateStats(isEjected, isExited, isKeyFinder);
    }
  }

  /**
   * Gets the name list corresponding to the given list of players
   * @param players the given list of players
   * @return a list of strings for the names of each players
   *                **/
  private List<String> getNameListFromPlayers(List<Player> players) {
    List<String> nameList = new ArrayList<>();
    for (Player player : players) {
      nameList.add(player.getName());
    }
    return nameList;
  }


  /**
   * Notifies every user of all the user statistics at the end of the game
   */
  private void notifyAllUsersGameEnd() {
    JSONObject endGameNotification = new JSONObject();
    endGameNotification.put("type", "end-game");
    JSONArray playerScoreList = new JSONArray();
    for (User user: this.users) {
      JSONObject playerScoreObject = new JSONObject();
      playerScoreObject.put("type", user.getName());
      playerScoreObject.put("exits", user.getNumExits());
      playerScoreObject.put("ejects", user.getNumEjects());
      playerScoreObject.put("keys", user.getNumKeysCollected());
      playerScoreList.put(playerScoreObject);
    }
    endGameNotification.put("scores", playerScoreList);
    for (User user : this.users) {
      user.send(endGameNotification.toString());
    }
  }


  /**
   * Sends each remote user the start notification
   *  {"type": "start-level",
   *   "level": (natural),
   *   "players": (name-list)
   *  }
   * @param currentLevel
   */
  private void notifyAllUsersLevelStart(int currentLevel) {
    JSONObject startNotification = new JSONObject();
    startNotification.put("type", "start-level");
    startNotification.put("level", currentLevel);
    JSONArray nameList = new JSONArray();
    for (User user : this.users) {
      nameList.put(user.getName());
    }
    startNotification.put("players", nameList);

    for(User user : this.users) {
      user.send(startNotification.toString());
    }
  }

  /**
   * Notifies all the users of the levels end
   */
  private void notifyAllUsersLevelEnd() {
    JSONObject endNotification = new JSONObject();
    endNotification.put("type", "end-level");

    String foundKey = gs.getKeyFinder() == null ? null : gs.getKeyFinder().getName();
    endNotification.put("key", foundKey);

    List<Player> exitedPlayers = gs.getExitedPlayers();
    JSONArray exitedPlayerNames = new JSONArray();
    for (Player player : exitedPlayers) {
      exitedPlayerNames.put(player.getName());
    }
    endNotification.put("exits", exitedPlayerNames);

    List<Player> ejectedPlayers = gs.getEjectedPlayers();
    JSONArray ejectedPlayerNames = new JSONArray();
    for (Player player : ejectedPlayers) {
      ejectedPlayerNames.put(player.getName());
    }
    endNotification.put("ejects", ejectedPlayerNames);

    for(User user : this.users) {
      user.send(endNotification.toString());
    }
  }

  /**
   * This method starts the game by initializing the GameState. This entails setting the level and
   * creating Player objects to represent each User object. Returns the gamecondition once ALL PLAYERS HAVE EXITED.
   *
   * @param level the actual level to play the game with
   * @param levelNumber the level number to run
   */
  @Override
  public String startLocalLevel(Level level, int levelNumber, boolean observeValue) {
    this.gs = new GameStateModel(level);
    List<Actor> players = userListToActors();
    // add adversaries to the game manager using the assignment specs
    addLocalAdversaries(levelNumber, level);
    List<Actor> actorAdversaries = snarlAdversaryListToActors();

    Map<String, Posn> actorNameToLocation =
            this.gs.initLocalGameState(players, actorAdversaries, level.getExitKeyPosition());

    this.gs.setCurrentLevelNumber(levelNumber);
    this.gs.setTotalLevels(this.levels.size());

    this.useActorsToSetUserPositionsAndSurroundings(actorNameToLocation);

    Scanner scanner = new Scanner(System.in);

    this.updateEveryone(null);

    // run game
    while (!ruleChecker.isLevelEnd(this.gs)) {
      String interactionType = this.promptPlayerTurn(scanner);
      this.updateEveryone(interactionType);
      this.updateTurn();
      if (observeValue) {
        System.out.println(this.gs.getLevel().createLevelString());
      }
    }
    // return the gameCondition
    return ruleChecker.getGameCondition(this.gs);
  }

  /**
   * This method converts each user to an actor and returns the list of converted actors.
   * @return List of actors corresponding to the users playing the game
   */
  private List<Actor> userListToActors() {
    List<Actor> players = new ArrayList<>();
    for (User user : this.users) {
      user.setExitable(false);
      Actor player = new Player(user.getName());
      players.add(player);
    }
    return players;
  }

  /**
   * This method converts each snarlAdversary to an actor and returns the list of converted actors.
   * @return List of actors corresponding to the snarlAdversaries in the game
   */
  private List<Actor> snarlAdversaryListToActors() {
    List<Actor> actorAdversaries = new ArrayList<>();
    for (SnarlAdversary snarlAdversary : this.adversaries) {
      Adversary gameAdversary = new Adversary(snarlAdversary.getType(), snarlAdversary.getName());
      actorAdversaries.add(gameAdversary);
    }
    return actorAdversaries;
  }

  private void printEndResult(String gameCondition, int levelNumber) {
    int exitNumber = levelNumber - (startLevel - 1);
    int keyNumber = levelNumber - (startLevel - 1);
    if (gameCondition.equals("Win")) {
      System.out.println("You won Snarl!");
    } else {
      System.out.println("You lost :(");
      exitNumber--;
      keyNumber = this.gs.isExitable() ? keyNumber : keyNumber - 1;
    }
    // note, only one player is in the game right now. Notification will be more complicated
    // for larger games
    System.out.println("Player " + users.get(0).getName() + " exited " + exitNumber + " levels and collected " + keyNumber + " keys.");
  }

  /**
   * this method updates everyone (users and snarlAdversaries)
   */
  private void updateEveryone(String interactionType) {
    this.updateUsers(interactionType);
    this.updateSnarlAdversaries();
  }

  private void updateSnarlAdversaries() {
    Map<Posn, Adversary> adversaryPosnMap = this.generateAdversaryMap();
    for (SnarlAdversary snarlAdversary : this.adversaries) {
      snarlAdversary.update(gs.getLevel(), adversaryPosnMap);
    }
  }

  /**
   * This method contacts every user via their update method and provides them with the newest
   * relevant information, namely their new visible tiles and whether or not the level is exitable.
   * @param interactionType describes what happened last move
   */
  @Override
  public void updateUsers(String interactionType) {
    List<String> remainingPlayers = getRemainingPlayers();
    String event = this.buildEvent(interactionType);
    for (User user : this.users) {
      user.update(this.gs.getSurroundingsForPosn(user.getCurrentPosition()), this.gs.isExitable(), user.getCurrentPosition(), remainingPlayers, event);
    }
  }


  /**
   * Builds the event description that is sent as a message to the user
   * @param interactionType
   * @return
   */
  private String buildEvent(String interactionType) {
    String interactionTypeDescription = interpretInteractionType(interactionType);
    String event = "";
    if (turn < this.users.size()) {
      event =  "Player " + this.users.get(turn).getName() + " " + interactionTypeDescription;
    } else {
      event = "Adversary " + this.adversaries.get(turn - this.users.size()).getName() + " " + interactionTypeDescription;
    }
    return event;
  }


  /**
   * Generates a map of game object adversaries to their positions
   * @return a map of adversaries to positions
   */
  private Map<Posn, Adversary> generateAdversaryMap() {
    Map<Posn, Adversary> posnAdversaryMap = new HashMap<>();
    List<Actor> adversaries = this.gs.getActors(false);
    for (Actor adversary : adversaries) {
      posnAdversaryMap.put(adversary.getPosition(), (Adversary) adversary);
    }
    return posnAdversaryMap;
  }

  /**
   * Generates a map of game object players to their positions
   * @return a map of players to their current positions
   */
  private Map<Posn, Player> generatePlayerMap() {
    Map<Posn, Player> posnPlayerMap = new HashMap<>();
    List<Actor> players = this.gs.getActors(true);
    for (Actor player : players) {
      posnPlayerMap.put(player.getPosition(), (Player) player);
    }
    return posnPlayerMap;
  }


  /**
   * The purpose of this method is match the players and adversaries to their User and
   * SnarlAdversary components and assign them the game object's current position
   *
   * @param actorNameToPosn is the map of actor names to their current position
   */
  private void useActorsToSetUserPositionsAndSurroundings(Map<String, Posn> actorNameToPosn) {
    for (User user : this.users) {
      Posn currentPosition = actorNameToPosn.get(user.getName());
      user.setCurrentPosition(currentPosition);
      user.setSurroundings(this.gs.getSurroundingsForPosn(currentPosition));
    }
    for (SnarlAdversary snarlAdversary : this.adversaries) {
      Posn currentPosition = actorNameToPosn.get(snarlAdversary.getName());
      snarlAdversary.setCurrentPosition(currentPosition);
    }
  }

  /**
   * Clear the adversaries list and compute the number of zombies and ghosts to be added and add
   * them to the given level.
   *
   * @param l     the current level number
   * @param level the provided level the adversaries are added
   */
  private void addLocalAdversaries(int l, Level level) {
    this.adversaries = new ArrayList<>();
    int numZombies = Math.floorDiv(l, 2) + 1;
    int numGhosts = Math.floorDiv((l - 1), 2);
    for (int j = 0; j < numZombies; j++) {
      Zombie frank = new Zombie(level, null);
      frank.setName("Zombie " + (j + 1));
      this.adversaries.add(frank);
    }
    for (int g = 0; g < numGhosts; g++) {
      Ghost casper = new Ghost(level, null);
      casper.setName("Ghost " + g + 1);
      this.adversaries.add(casper);
    }
  }

  /**
   * Starts the game trace for the testing task.
   *
   * @param level the level to start the game with
   * @param posns the list of intital positions for actors. The first n positions are for the users
   *              and any subsequent posn is for an Adversary we create
   */
  @Override
  public void startGameTrace(Level level, List<Posn> posns) {
    GameState gameState = new GameStateModel(level);
    this.gs = gameState;

    List<Actor> players = new ArrayList<>();
    List<Actor> actorAdversaries = new ArrayList<>();

    for (int i = 0; i < users.size(); i++) {
      Actor player = new Player(users.get(i).getName());
      users.get(i).setCurrentPosition(posns.get(i));
      player.setPosition(posns.get(i));
      players.add(player);
    }
    for (int i = users.size(); i < posns.size(); i++) {
      Adversary adversary = new Adversary("Zombie", String.valueOf(i));
      adversary.setPosition(posns.get(i));
      actorAdversaries.add(adversary);
    }
    gameState.initGameStateWhereActorsHavePositions(players, actorAdversaries, level.getExitKeyPosition());

    this.nextUser = getRemainingPlayers().get(0);

    // give each User an initial surround
    for (int i = 0; i < users.size(); i++) {
      users.get(i).setSurroundings(gameState.getSurroundingsForPosn(players.get(i).getPosition()));
    }

  }

  /**
   * Creates a message to tack onto the end of the event. The message is based on the given
   * interaction type.
   * @param interactionType
   * @return
   */
  private String interpretInteractionType(String interactionType) {
    if (interactionType != null) {
      switch (interactionType) {
        case "OK":
          return "moved.";
        case "Eject":
          return "was ejected.";
        case "Adversary Eject":
          return "ejected a player.";
        case "Exit":
          return "exited the level";
        case "Key":
          return "found the key.";
        default:
          return "is waiting for the other players.";
      }
    }
    // only ever returned for the first update describing the init conditions
    return "will move first";
  }

  /**
   * This method prompts the next player to take their turn. It then executes the action returned
   * from the turn. This method is expected to run continously throughout the levels duration.
   * @return
   */
  @Override
  public String promptPlayerTurn(Scanner scanner) {
    String interactionType = "";
    // check if it is the user's turn
    if (turn < this.users.size()) {
      // ask for input again if invalid
      // check if they have been ejected/exited
      User user = this.users.get(turn);
      System.out.println("It is user " + user.getName() + "'s turn");
      List<String> ejectedPlayers = this.getNameListFromPlayers(gs.getEjectedPlayers());
      List<String> exitedPlayers = this.getNameListFromPlayers(gs.getExitedPlayers());
      if (ejectedPlayers.size() > 0) {
        System.out.println(ejectedPlayers.get(0));
      }
      if (!ejectedPlayers.contains(user.getName()) &&  !exitedPlayers.contains(user.getName())) {
        MoveAction action = (MoveAction) user.turn(scanner);
        this.executeAction(action, user);
        interactionType = action.getInteractionType();
      }
    }
    // check if it is an adversaries turn
    if (this.turn >= this.users.size()) {
      SnarlAdversary snarlAdversary = adversaries.get(turn - this.users.size());
      MoveAction action = (MoveAction) snarlAdversary.turn(this.generatePlayerMap(), this.generateAdversaryMap());
      this.executeAction(action, snarlAdversary);
      interactionType = action.getInteractionType();

    }
    return interactionType;
  }

  /**
   * Called after each turn, increased the turn count or sets it to zero
   */
  public void updateTurn() {
    System.out.println("turn: " + turn);
    if (this.turn == this.users.size() + this.adversaries.size() - 1) {
      this.turn = 0;
    } else {
      this.turn++;
    }
  }

  /**
   * This method executes the given action on the game state.
   * @param user
   * @param action
   */
  @Override
  public void executeAction(Action action, User user) {
      MoveAction moveAction = (MoveAction) action;
      this.gs.handleMoveAction(moveAction, ruleChecker);
      String interactionType = moveAction.getInteractionType();
      // if playing a remote game of snarl, this will send the result to the user
      this.users.get(turn).send(interactionType);
      if (interactionType.equals("Eject")) {
        System.out.println("Player " + user.getName() + " was expelled");
      } else if (interactionType.equals("Key")) {
        System.out.println("Player " + user.getName() + " found the key");
      }
      else if (interactionType.equals("Exit")) {
        System.out.println("Player " + user.getName() + " exited");
      }
      user.setCurrentPosition(moveAction.getDestination());
  }

  /**
   * This executes the action of the give snarlAdversary
   * @param action
   * @param snarlAdversary indicates the snarlAdversary involved in the action
   */
  @Override
  public void executeAction(Action action, SnarlAdversary snarlAdversary) {
    MoveAction moveAction = (MoveAction) action;
    this.gs.handleMoveAction(moveAction, snarlAdversary);
    if (moveAction.getInteractionType().equals("Eject")) {
      String mostRecentEjectionName = this.gs.getExitedPlayers().get(this.gs.getExitedPlayers().size() - 1).getName();
      System.out.println("Player " + mostRecentEjectionName + " was expelled");
    }
  }

  /**
   * Gets the user with the given string
   *
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
   * Play out the move of the User's whose turn it is using the move input. We are assuming it is a
   * users turn, if not, adverseraries are skipped. This method is only used by testManager at this
   * specific time.
   *
   * @param managerTrace the JSONArray to add the traces to
   * @return returns whether or not the input stream for the user moving has been exahusted
   */
  @Override
  public boolean playOutMove(JSONArray managerTrace) {
    int indexOfNextUser = getIndexOfNameInUsers(nextUser);

    User user = this.users.get(indexOfNextUser);
    // we need the index of the player with the usernext name
    List<Posn> movesList = this.moveInput.get(indexOfNextUser);

    // create the move action, validate it, if its not valid, then create next move action
    // if it is valid, execute move and return the JSONArray result
    if (tryOutMoves(managerTrace, movesList, user)) {
      return true;
    }

    List<String> remainingPlayers = this.getRemainingPlayers();
    if (remainingPlayers.size() == 0) {
      return false;
    }

    // if the last player just went, go back to the first player and skip the adversaries
    if (nextUser.equals(remainingPlayers.get(remainingPlayers.size() - 1))) {
      nextUser = remainingPlayers.get(0);
    } else {
      int currentIndex = remainingPlayers.indexOf(nextUser);
      nextUser = remainingPlayers.get(currentIndex + 1);
    }
    return false;
  }

  /**
   * This method tries out moves from the given move list and returns true if none of the moves
   * work
   *
   * @param managerTrace
   * @param movesList
   * @param user
   * @return true if the moves run out
   */
  private boolean tryOutMoves(JSONArray managerTrace, List<Posn> movesList, User user) {
    boolean moveWorked = false;
    while (!moveWorked) {
      // if the moves list is exhausted, return true
      if (movesList.size() == 0) {
        return true;
      }
      // create the move action
      Posn dest = movesList.get(0);
      MoveAction move = new MoveAction(dest, user.getCurrentPosition());
      // handleMoveAction returns true if the move is valid, false if it is not
      moveWorked = gs.handleMoveAction(move, this.ruleChecker);
      // if the move worked, set the new current position as the destination and update the user's new surroundings
      if (moveWorked) {
        user.setCurrentPosition(move.getDestination());
        user.setSurroundings(gs.getSurroundingsForPosn((user.getCurrentPosition())));
      }

      JSONArray moveResponse = buildMoveResponseJSON(move, user.getName());
      managerTrace.put(moveResponse);

      // break if the level has been completed
      if (move.getInteractionType().equals("Exit")) {
        break;
      }
      movesList.remove(dest);
    }
    return false;
  }

  /**
   * This method returns the index of the given name in the list of users.
   *
   * @param name the given name of the user to find
   * @return the index of the user with the given name
   */
  private int getIndexOfNameInUsers(String name) {
    int index = 0;
    for (User user : this.users) {
      if (user.getName().equals(name)) {
        return index;
      }
      index++;
    }
    return -1;
  }

  /**
   * This method builds a valid move JSONArray response
   * <p>
   * "OK", meaning “the move was valid, nothing happened” "Key", meaning “the move was valid, player
   * collected the key” "Exit", meaning “the move was valid, player exited” "Eject", meaning “the
   * move was valid, player was ejected” "Invalid", meaning “the move was invalid”
   * <p>
   * build response [ name, actor-move, result]
   *
   * @param moveAction the actor-move the response is being built for
   * @param name       the name of the user completing the move
   * @return A JSONArray representing a response
   */
  private JSONArray buildMoveResponseJSON(MoveAction moveAction, String name) {
    JSONArray moveResponse = new JSONArray();
    moveResponse.put(name);
    moveResponse.put(buildMoveJSONObject(moveAction));
    moveResponse.put(moveAction.getInteractionType());
    return moveResponse;
  }

  /**
   * Creates the properly formatted move JSON object from a MoveAction a move json looks like {
   * type: "move", to: [0, 0] }
   *
   * @param moveAction
   * @return
   */
  private JSONObject buildMoveJSONObject(MoveAction moveAction) {
    JSONObject moveObject = new JSONObject();
    moveObject.put("type", "move");
    if (!moveAction.getDestination().equals(moveAction.getCurrentPosition())) {
      moveObject.put("to", posnToJson(moveAction.getDestination()));
    } else {
      moveObject.put("to", JSONObject.NULL);
    }
    return moveObject;
  }


  /**
   * Returns the names of the remaining players still in the game
   *
   * @return A list of names of players still in the game
   */
  @Override
  public List<String> getRemainingPlayers() {
    List<Player> exitedPlayers = gs.getExitedPlayers();
    List<String> exitedPlayersNames = new ArrayList<>();
    for (Player player : exitedPlayers) {
      exitedPlayersNames.add(player.getName());
    }

    List<String> remainingPlayers = new ArrayList<>();
    for (User user : users) {
      String name = user.getName();
      if (!exitedPlayersNames.contains(name)) {
        remainingPlayers.add(name);
      }
    }
    return remainingPlayers;
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

  public List<SnarlAdversary> getAdversaries() {
    return adversaries;
  }

  public void setAdversaries(List<SnarlAdversary> adversaries) {
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
   *
   * @param actorMoveListList the positions represent the destination for each move
   */
  @Override
  public void setMoveInput(ArrayList<ArrayList<Posn>> actorMoveListList) {
    this.moveInput = actorMoveListList;
  }

  /**
   * Creates a JSONArray containing the coordinates for the given GameObjects.Posn
   *
   * @param point the given position
   * @return JSONArray containing the coordinates
   */
  public static JSONArray posnToJson(Posn point) {
    int row = point.getRow();
    int col = point.getCol();
    JSONArray pointList = new JSONArray();
    pointList.put(row);
    pointList.put(col);
    return pointList;
  }


  public List<Level> getLevels() {
    return levels;
  }

  public void setLevels(List<Level> levels) {
    this.levels = levels;
  }


  public int getStartLevel() {
    return startLevel;
  }

  public void setStartLevel(int startLevel) {
    this.startLevel = startLevel;
  }
}
