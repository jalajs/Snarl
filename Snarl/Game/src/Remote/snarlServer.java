package Remote;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

import GameManager.GameManagerClass;
import GameManager.GameManager;
import GameObjects.Level;
import JSONUtils.RemoteUtils;

public class snarlServer {
  private static int clientValue = 4;
  private static String addressValue = "127.0.0.1";
  private static int portValue = 45678;
  private static String levelValue = "snarl.levels";
  private static int delayValue = 10000;
  private static boolean observerValue = false;
  private static int startLevel = 1;

  public static void main(String[] args) throws IOException {
    Options options = buildCommandOptions();

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);

      levelValue = cmd.getOptionValue("levels") == null ? levelValue : cmd.getOptionValue("levels");
      clientValue =  cmd.getOptionValue("clients") == null ? clientValue : Integer.parseInt(cmd.getOptionValue("clients"));
      delayValue = cmd.getOptionValue("wait") == null ? delayValue : Integer.parseInt(cmd.getOptionValue("wait"));
      addressValue = cmd.getOptionValue("address") == null ? addressValue : cmd.getOptionValue("address");
      portValue = cmd.getOptionValue("port") == null ? portValue : Integer.parseInt(cmd.getOptionValue("port"));
      observerValue = cmd.hasOption("observe");

    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);

      System.exit(1);
    }

    List<Level> levels = RemoteUtils.parseLevelsFile(levelValue);

    GameManager gameManager = new GameManagerClass(levels, startLevel);

    // todo: give player number idenitifier and make exit door look different

    Server server = new Server(addressValue, portValue, gameManager, delayValue, observerValue, clientValue);
    server.run();
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

      Option clientInput = new Option("c", "clients", true, "The max number of clients");
      clientInput.setRequired(false);
      options.addOption(clientInput);

      Option waitInput = new Option("w", "wait", true, "the number of seconds to wait for next client to connect");
      waitInput.setRequired(false);
      options.addOption(waitInput);

      Option observerInput = new Option("o", "observe", false, "observer view");
      observerInput.setRequired(false);
      options.addOption(observerInput);

      Option ipInput = new Option("a", "address", true, "the IP address on which the server should listen for connections");
      ipInput.setRequired(false);
      options.addOption(ipInput);

      Option portInput = new Option("p", "port", true, "the port number the server will listen on");
      portInput.setRequired(false);
      options.addOption(portInput);

      return options;
    }

}
