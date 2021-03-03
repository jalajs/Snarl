Dear friends at Growl Inc, <br>
<br>
The following memo outlines the Player component, which will be referred to from here on out as the User. The User represents the human/user behind the keyboard in the game.
The player component receives updates from the GameManager, and communicates its actions to the GameManager when
it is its turn. These are the User's field:
<br>
- List<ArrayList <Tile <Tile>>> visibleTiles
    - Represents the grid that the user can see at this instance of the game
    
- List<Collectables<Collectables>> inventory
    - The inventory/collectables that a Player has collected 
    
- Posn playerPosn
    - The Player's current location in relation to the level's origin

<br>
With that data in mind, it's planned interface will contain the following methods:
<br>

- Action turn()
    - Returns action to gameManager 
    - Action is an interface which represents a User's move / interaction
        - At the moment we for see implementing classes Move, DoNothing, Exit, and GrabCollectable. Each class will have fields pertinent to its function and all will implement the method execute().
        - We envision these Actions will be built at the Tile level, as a Tile know how a player has to interact with it. On every turn, a user will select one of it's visible (and legal) tiles to move to. On selection, the Action will be built and returned to the User, who will return it to the GameManager. A user can of course decide to do nothing instead, in which case a DoNothing Action will be returned.
        - The Tile will build the Action with respect to the following order of interactions: interact with the enemy, interact with the key, and then interact with the exit. 
- void updatePlayersView(List<ArrayList <Tile <Tile>>> visible, Posn playerPosn)
    - Provide Player with the updated display of visible tiles from which it can indicate its move
- void updatePlayerInventory(Collectable c)
    - Updates the user's inventory the collectable picked up by the player

<br>
Please let us know if you have questions about the contents of this memo.
<br>
Best regards,
<br>
  Megan Larson & Jalaj Signh
