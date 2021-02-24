Dear friends at Growl Inc, <br>

The purpose of this memo is to outline the RuleChecker component. The RuleChecker component is tasked with performing all validations for Snarl. We anticipate the component's interface will need the following behavoirs.<br>

- Boolean isLevelEnd(Level level)
    - A level is over if the exit door is unlocked and a player goes through it or if all players in the level are expelled. 
- Boolean isGameEnd()
    - The game is over if all the players are expelled in a given level (resulting in a loss for the players)
    - Any player reaches the unlocked exit of the final level of the dungeon (win)
- Boolean isGameStateValid(GameState gs)
    - Ensures given GameState is compliant with logics of game
    - GameState is invalid if:
      - Any actors are on walls or otherwise in bad positions (use map)
      - Any collectables are on bad positions
      - The level is invalid
- Boolean isMoveValid(Actor actor, Posn destination)
   - Invalid if too far, wall, empty space (no tile) or interaction is invalid (calls below method)
- Boolean isInteractionValid(Actor actor, Tile tile)
   - Point is to see if player can interact with the Adversary or Door or Collectable on tile
   - Players cannot interact with other players
   - Actor must be on the tile in order to interact with the Collectable or occupier
      - I.e. they must interact with the object on the tile they choose and no other tile
   - Actors may have specific rules that this method would respect
      - I.e. all Adversary classes will have boolean canWalkThroughWall(), so that they can behave differently as needed
