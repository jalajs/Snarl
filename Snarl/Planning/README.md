## Welcome to SNARL  
### Server Setup Instructions
-   Start the snarlServer by running ./snarlServer and providing any optional arguments.
-   The server's observer can be used in a terminal    
-   The server will wait for clients to connect on its specified address and port. Once the max number of 
    clients have connected or the timeout has lapsed, the server will start running a game.

### Client Setup Instructions
-   Start the snarlClient by running ./snarlClient and providing any optional arguments.
-   The client can be used in a terminal    
-   The users will be prompted to enter the name and connect to the server on the given address and port
        - the user will be disconnected if they enter an existing/duplicate name from the game
-   After connecting, the client should follow the assignment's protocol and allow the user to play a 
    Snarl game on the server
    
### Game Play Instructions

- After all users are registered, the first player will be prompted for there move and showed their 5x5
  grid.
- To move, please type the coordinate (row, col) you would like to move. Separate the row and col 
  numbers by a single space with no punctuations.
- If the move you give is invalid, you will be prompted. We allow for infinite invalid moves.  
- After every user's turn, each user will see their 5x5 layout and get an update message on what just 
happened
  
          
### Key
-   1 - 4 = player
-   X = wall
-   . = walkable tile
-   | = door
-   K = exit key
-   Z = zombie
-   G = ghost
