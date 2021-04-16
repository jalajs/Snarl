package Remote;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import GameManager.GameManager;
import User.RemoteUser;
import User.User;

public class Server {
  private static GameManager gameManager;
  private static ServerSocket serverSocket;
  private static List<Socket> playerSockets;
  private static int maxNumber = 4;
  private static int minNumber = 1;
  private static String ipAddress = "127.0.0.1";
  private static int port = 45678;
  private static int delayValue;
  private static boolean observe;
  private static List<String> existingNames;

  public Server(String hostName, int port, GameManager gameManager, int delayValue, boolean observe, int maxNumber) {
    try {
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println(e);
    }
    this.gameManager = gameManager;
    this.playerSockets = new ArrayList<>();
    this.delayValue = delayValue;
    this.observe = observe;
    this.maxNumber = maxNumber;
    this.existingNames = new ArrayList<>();
  }

  /**
   * This method runs the server, which in turn runs the game. The server first registers up to the
   * max number of players,
   *
   * @throws IOException
   */
  public void run() throws IOException {
    // listen for first player
    waitForPlayers(minNumber);
    // set the socket timeout
    serverSocket.setSoTimeout(delayValue);

    try {
      // listen for the next player
      waitForPlayers(maxNumber - minNumber);
    } catch (IOException sot) {
      System.out.println("No additional players");
    }

    gameManager.runRemoteGame(observe);

    for (Socket socket : playerSockets) {
      socket.close();
    }
    serverSocket.close();
  }


  private static void waitForPlayers(int numPlayers) throws IOException {
    for (int i = 0; i < numPlayers; i++) {
      Socket playerSocket = serverSocket.accept();
      playerSockets.add(playerSocket);
      RemoteUser user = new RemoteUser(playerSocket);
      JSONObject serverWelcome = new JSONObject();
      serverWelcome.put("type", "welcome");
      serverWelcome.put("info", "Laressea");
      user.send(serverWelcome.toString());
      String name = user.promptName();
      if (!existingNames.contains(name)) {
        gameManager.addRemotePlayer(user);
        existingNames.add(name);
      } else {
        user.send("invalid_name");
      }
    }
  }
}
