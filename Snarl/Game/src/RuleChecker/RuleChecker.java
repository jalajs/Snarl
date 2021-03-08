package RuleChecker;


import GameObjects.Actor;
import GameObjects.Posn;
import GameObjects.Tile;
import GameObjects.Level;
import GameState.GameState;

public interface RuleChecker {

  /**
   * A level is over if the exit door is unlocked and a player goes through it or
   *  if all players in the level are expelled.
   * @param GameState the current gameState
   * @return
   */
  boolean isLevelEnd(GameState gameState);

  /**
   * Determines if the entire game is over. The game is over if all the players are expelled in a given level,
   * or if any player reaches the unlocked exit of the final level of the dungeon.
   *
   * @return true if the game is over
   */
  boolean isGameEnd(GameState gameState);

  /**
   * Ensures that the given GameState is compliant with logics of game. A GameState is invalid if any
   * actors are on walls or otherwise in bad positions, any collectables are in bad positions, or
   * if the level is invalid.
   *
   * @param gameState is the GameState to be validate
   * @return true if the GameState is valid
   */
  boolean isGameStateValid(GameState gameState);

  /**
   * Invalid if too far, wall, empty space (no tile) or interaction is invalid
   * A move is to far if the desired tile is more than two cardinal moves away
   * @param actor the given actor moving
   * @param destination the destination it is trying to move to
   * @param level is the level in which the move must be validated
   * @return whether or not there move is valid
   */
  boolean isMoveValid(Level level, Actor actor, Posn destination);

  /**
   * See if an actor can interact with the Adversary or Door or Collectable on the given tile
   * Players cannot interact with other players, but adversaries can interact with players
   * Actor must be on the tile in order to interact with the Collectable or occupier
   * I.e. they must interact with the object on the tile they choose and no other tile
   * @param actor the actor trying to do the interaction
   * @param tile the tile the interaction is on
   * @param isExitable indicates whether the exit door is interactable
   * @return whether or not the interaction is valid
   */
  boolean isInteractionValid(boolean isExitable, Actor actor, Tile tile);

}
