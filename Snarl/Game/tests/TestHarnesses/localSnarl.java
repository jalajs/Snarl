package TestHarnesses;

import org.apache.commons.cli.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import GameManager.GameManager;
import GameManager.GameManagerClass;
import GameObjects.Hallway;
import GameObjects.Level;
import GameObjects.Posn;
import GameObjects.Room;

public class localSnarl {
  private static testLevel testLevel;
  private static testRoom testRoom;
  private static int defaultPlayersOption = 1;
  private static int defaultStartOption = 1;
  private static String defaultLevelOption = "snarl.levels";

  public static void main(String[] args) {
    Options options = buildCommandOptions();

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);

      String levelValue = cmd.getOptionValue("levels") == null ? defaultLevelOption : cmd.getOptionValue("levels");
      int playersValue = computeNumberOfPlayers(cmd.getOptionValue("players"));
      int startValue = cmd.getOptionValue("start") == null ? defaultStartOption : Integer.parseInt(cmd.getOptionValue("start"));
      boolean observerValue = cmd.hasOption("observe");

      List<Level> levels = parseLevelsFile(levelValue);
      GameManager gameManager = new GameManagerClass(levels, startValue);

      // register users
      // prompt user for userName
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter a user name to join the game: ");
      String userName = scanner.nextLine();
      gameManager.addPlayer(userName);
      // delegate game play to game manager
      gameManager.runLocalGame(observerValue);

    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);

      System.exit(1);
    }

  }

  /**
   * This method ensures Snarl is a single player game and throws an error if more than one player is asked
   * of in the command line.
   *
   * @param playerOptionValue
   * @return
   */
  private static int computeNumberOfPlayers(String playerOptionValue) {
    int numberOfPlayers = playerOptionValue == null ? defaultPlayersOption : Integer.parseInt(playerOptionValue);
    if (numberOfPlayers != 1) {
      System.out.println("Invalid number of players. Snarl is currently only a single player game.");
      System.exit(1);
    }
    return numberOfPlayers;
  }

  /**
   * Build the possible options our command line program takes in
   * @return an Options option with all of our options
   */
  private static Options buildCommandOptions() {
    Options options = new Options();
    Option levelInput = new Option("l", "levels", true, "the levels for the game");
    levelInput.setRequired(false);
    options.addOption(levelInput);

    Option playerInput = new Option("p", "players", true, "The number of players");
    playerInput.setRequired(false);
    options.addOption(playerInput);

    Option startInput = new Option("s", "start", true, "The level from which to start the game");
    startInput.setRequired(false);
    options.addOption(startInput);

    Option observerInput = new Option("o", "observe", false, "observer view");
    observerInput.setRequired(false);
    options.addOption(observerInput);
    return options;
  }

  /**
   * Parse the given levels file
   * @param levelValue the name of the levels file
   * @return a list of levels
   */
  private static List<Level> parseLevelsFile(String levelValue) {
    List<Level> levels = new ArrayList<>();
    try {
      File file = new File(levelValue);

      Scanner scanner = new Scanner(file.getAbsoluteFile());
      String tokenerSource = "";
      while (scanner.hasNextLine()) {
        String next = scanner.nextLine();
        tokenerSource += next + ' ';
      }
      scanner.close();

      JSONTokener tokener = new JSONTokener(tokenerSource.trim());
      buildLevels(tokener, levels);

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      System.out.println(levelValue +" is an invalid level file. Please try again.");
      System.exit(1);
    }
    return levels;
  }

  /**
   * This method mutates the given level List so that it contains all the parsed
   * out levels described in the JSONTokener
   *
   * @param tokener
   * @param levels
   */
  private static void buildLevels(JSONTokener tokener, List<Level> levels) {
    int levelNumber = (int) tokener.nextValue();
    // parse out commands
    while (tokener.more()) {
      Object value = tokener.nextValue();
      String valueString = value.toString();

      JSONObject jsonLevel = new JSONObject(valueString);
      JSONArray jsonRooms = (JSONArray) jsonLevel.get("rooms");
      JSONArray jsonHallways = (JSONArray) jsonLevel.get("hallways");
      JSONArray jsonObjects = (JSONArray) jsonLevel.get("objects");

      List<Room> rooms = testLevel.parseRooms(jsonRooms);
      List<Hallway> hallways = testLevel.parseHallways(jsonHallways);
      List<Posn> objectPositions = testLevel.parseObjects(jsonObjects);

      Level level = testLevel.buildLevel(rooms, hallways, objectPositions);
      levels.add(level);
    }
  }
}


