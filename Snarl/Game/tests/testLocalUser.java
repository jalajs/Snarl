import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GameObjects.Adversary;
import GameObjects.Player;
import GameObjects.Posn;
import GameObjects.Tile;
import User.LocalUser;

public class testLocalUser {
  public static void main(String[] args) {

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
    adversaryTile.setOccupier(new Adversary("Zombie","Jimbo"));

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

    user.turn();

  }
}
