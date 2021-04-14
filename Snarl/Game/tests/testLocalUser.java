

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Action.MoveAction;
import GameObjects.Adversary;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;
import User.LocalUser;

public class testLocalUser {
  /**
   * Testing the user being interactively prompted to chose a destination.
   *
   * @param args the user input
   */
  public static void main(String[] args) {
    System.out.println("NOTE: testLocalUser is designed to first test a simple move, then an eject. After" +
            "running one simple move (with however many invalids you like), run the move (2,2) ");
    LocalUser user = new LocalUser("Bob");
    List<List<Tile>> initSurroundings = new ArrayList<>();
    List<Tile> row1 = new ArrayList<>();
    List<Tile> row2 = new ArrayList<>();
    List<Tile> row3 = new ArrayList<>();

    Tile wallTile = new Tile(true);
    Tile traversableTile = new Tile(false);
    Tile playerTile = new Tile(false);
    Tile adversaryTile = new Tile(false);
    playerTile.setOccupier(new Player("Bob"));
    adversaryTile.setOccupier(new Adversary("Zombie", "Jimbo"));

    row1.add(wallTile);
    row1.add(playerTile);
    row1.add(wallTile);

    row2.add(traversableTile);
    row2.add(traversableTile);
    row2.add(traversableTile);

    row3.add(wallTile);
    row3.add(traversableTile);
    row3.add(adversaryTile);

    initSurroundings.add(row1);
    initSurroundings.add(row2);
    initSurroundings.add(row3);

    user.setSurroundings(initSurroundings);
    user.setCurrentPosition(new Posn(0, 1));

    MoveAction moveAction = (MoveAction) user.turn(new Scanner(System.in));

    // Testing that the user provides the correct action when turn is called
    System.out.print("Is this the position you gave us? ");
    System.out.println("(" + moveAction.getDestination().getRow() + ", "
            + moveAction.getDestination().getCol() + ")");
  }
}
