
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import GameObjects.Actor;
import GameObjects.Adversary;
import GameObjects.Level;
import GameObjects.Player;
import GameObjects.Posn;
import GameState.GameStateModel;
import Observer.LocalObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalObserverTest {
  private final TestUtils testUtils = new TestUtils();

  @Test
  public void testUpdate() {
    Level initLevel = testUtils.createComplicatedLevel();
    GameStateModel simpleGameState = new GameStateModel(initLevel);

    List<Actor> players = new ArrayList<>();
    List<Actor> adversaries = new ArrayList<>();

    Player player1 = new Player();
    Player player2 = new Player();
    Adversary adversary1 = new Adversary();
    Adversary adversary2 = new Adversary();

    players.add(player1);
    players.add(player2);
    adversaries.add(adversary1);
    adversaries.add(adversary2);

    simpleGameState.initGameState(players, adversaries, new Posn(0, 9));

    LocalObserver localObserver = new LocalObserver();

    localObserver.update(simpleGameState);
  }
}
