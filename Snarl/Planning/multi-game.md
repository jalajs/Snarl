# #Multi Game
### Code changes:
- SnarlServer:
  - We added a new command option called boolean --multi to run a multi game session.
- Server:
  - We nested the 'runRemoteGame' call in a while loop using a 'playAgain' variable
  - After each game, we ask the admin if they would like to exit the Game. If they type 'exit', 'playAgain' is set to false and the session ends.
- RemoteUser:
  - Fields were added to keep track of player stats across all games for the leader board.
  - A method was added to update the leader board stats by adding on the game specific stats to the running totals.
- GameManager:
  - No huge changes were needed in the GameManager since the multi game logic was in the server.
  - However, at the end of each level calls were made to the method that updates each user's leader board stats.  
- Client:
  - created a new way to render score-lists in a table fashion for the running leaderboard
  - adjustments to accommodate changes to protocol
### Protocol Changes
- added a "terminate" command that the server sends to the client when they want to shut down and not continue playing any new games
    - instead of exiting after an end-game object is sent, clients now close and exit when this command is sent
- modified end-game JSON object by adding a "leader-board" : (player-score-list) field which holds the running stats of the players from the games finished so far
    
