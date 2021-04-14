package Remote;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class snarlClient {
  private static String addressValue = "127.0.0.1";
  private static int portValue = 45678;

  public static void main(String[] args) throws IOException {
    Options options = buildCommandOptions();

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);
      addressValue = cmd.getOptionValue("address") == null ? addressValue : cmd.getOptionValue("address");
      portValue = cmd.getOptionValue("port") == null ? portValue : Integer.parseInt(cmd.getOptionValue("port"));

    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);

      System.exit(1);
    }

    Client client = new Client(addressValue, portValue);
    System.out.println("running the client");
    client.run();
    System.out.println("client is not running anymore");
  }

  /**
   * Build the possible options our command line program takes in
   * @return an Options option with all of our options
   */
  private static Options buildCommandOptions() {
    Options options = new Options();

    Option ipInput = new Option("a", "address", true, "the IP address on which the client should listen for connections");
    ipInput.setRequired(false);
    options.addOption(ipInput);

    Option portInput = new Option("p", "port", true, "the port number the client will listen on");
    portInput.setRequired(false);
    options.addOption(portInput);

    return options;
  }

}
