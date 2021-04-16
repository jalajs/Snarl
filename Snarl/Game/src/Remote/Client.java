package Remote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import Action.Action;
import GameObjects.Posn;
import GameObjects.Tile;
import JSONUtils.RemoteUtils;
import User.User;
import User.LocalUser;

public class Client {
    private static Socket socket;
    private static User user;
    private static Scanner userScanner = new Scanner(System.in);
    private static RemoteUtils utils = new RemoteUtils();
    private static InputStream input;
    private static BufferedReader reader;
    private static OutputStream output;
    private static PrintWriter writer;
    private static boolean alive;

    /**
     * This constructs a basic client that owns a LocalUser
     *
     * @param host
     * @param port
     * @throws IOException
     */
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(InetAddress.getByName(host), port);
        this.user = new LocalUser();
        this.input = this.socket.getInputStream();
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.output = this.socket.getOutputStream();
        this.writer = new PrintWriter(output, true);
        this.alive = true;
    }

    /**
     * Method that runs the client by continuously checking for messages from the server and sending
     * responses. If a null message is recieved, the client shuts down. If an empty message is recieved, the
     * client does nothing.
     */
    public void run() {
        while (alive) {
            String serverData = receive();
            if (serverData.equals("null")) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            } else if (!serverData.equals("")) {
                String response = executeServerRequest(serverData);
                if (!response.equals("{}")) {
                    send(response);
                }
            }
        }

    }

    /**
     * This method performs the server request. The protocol are specific.
     *
     * @param serverData
     * @return
     */
    private static String executeServerRequest(String serverData) {
        JSONObject response = new JSONObject();
        if (serverData.equals("name")) {
            return user.promptForName(userScanner);
        } else if (serverData.equals("move") || serverData.equals("Invalid")) {
            Action action = user.turn(userScanner);
            response.put("type", "move");
            response.put("to", utils.posnToJson(action.getDestination()));
            return response.toString();
        } else if (serverData.charAt(0) == '{') {
            JSONObject serverRequest = new JSONObject(serverData);
            String type = (String) serverRequest.get("type");
            switch (type) {
                case "welcome":
                    System.out.println((String) serverRequest.get("info"));
                    break;
                case "start-level":
                    displayStartLevel(serverRequest);
                    break;
                case "player-update":
                    displayUpdate(serverRequest);
                    break;
                case "end-level":
                    displayEndLevel(serverRequest);
                    break;
                case "end-game":
                    try {
                        System.out.println("The game is donezo! Thank you for playing SNARL.");
                        printScores((JSONArray) serverRequest.get("scores"));
                        alive = false;
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("did not let the socket close on end game :(");
                    }
                    break;
                default:
                    System.out.println("Something mysterious is happening");
                    break;
            }
        }
        return response.toString();
    }

    /**
     * Display the start level message to the user
     * @param serverRequest
     */
    private static void displayStartLevel(JSONObject serverRequest) {
        Posn currentPosition = utils.jsonToPosn((JSONArray) serverRequest.get("position"));
        user.setCurrentPosition(currentPosition);

        List<List<Tile>> surroundings = utils.jsonToSurroundings(serverRequest);
        user.setSurroundings(surroundings);

        String message = (String) serverRequest.get("message");
        if (message.contains("key")) {
            user.setExitable(true);
        }
        System.out.println("game update: " + message);
    }

    /**
     * Displays the update message to user. This method also updates isExitable if the
     * most recent update message is about a key.
     *
     * @param serverRequest
     */
    private static void displayUpdate(JSONObject serverRequest) {
        Posn currentPosition = utils.jsonToPosn((JSONArray) serverRequest.get("position"));
        user.setCurrentPosition(currentPosition);

        List<List<Tile>> surroundings = utils.jsonToSurroundings(serverRequest);
        user.setSurroundings(surroundings);

        String message = (String) serverRequest.get("message");
        if (message.contains("key")) {
            user.setExitable(true);
        }
        System.out.println("game update: " + message);
    }


    /**
     * Displays the end level messages to the user.
     *
     * @param serverRequest the end-level request object sent from the server
     */
    private static void displayEndLevel(JSONObject serverRequest) {
        System.out.println("Level is over");
        JSONArray exits = (JSONArray) serverRequest.get("exits");
        JSONArray ejects = (JSONArray) serverRequest.get("ejects");
        String keyFinder = (String) serverRequest.get("key");
        System.out.println("Player " + keyFinder + " found the key");
        System.out.println("Player(s) " + exits.toString().replace("]", "").replace("\"", "") + " exited");
        System.out.println("Player(s) " + ejects.toString().replace("]", "").replace("\"", "") + " ejected");

    }


    /**
     * This method prints the score list passed to client. This method is called in the
     * end game scenario
     *
     * @param scoreList
     */
    private static void printScores(JSONArray scoreList) {
        for (int i = 0; i < scoreList.length(); i++) {
            JSONObject scoreObject = (JSONObject) scoreList.get(i);
            String name = (String) scoreObject.get("name");
            int exits = (int) scoreObject.get("exits");
            int ejects = (int) scoreObject.get("ejects");
            int keys = (int) scoreObject.get("keys");
            System.out.println(name + ", you exited " + exits + " time(s), were ejected " + ejects + " time(s), and found " + keys + " keys");
        }
    }

    private void send(String message) {
        writer.println(message);
    }

    private String receive() {
        String message = "";
        try {
            message = message + reader.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
        return message;
    }
}
