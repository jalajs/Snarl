Dear friends at Growl Inc, <br>
<br>
The following memo outlines the GameManager component. The GameManager component is tasked with managing the game sequence.
Ergo, it must allow players to enter, generate the level, start the game, and manager each players turns. These are it's planned fields:
<br>
- Gamestate object
   - This GameState object represents the current time slice of the game. Please reference state.md if you have any questions.
- List<User<User>> players
   - This list represents all the players in the GameState
   - Please see player.md for more information on the User object
- Int turn 
  - This integer indicates which player's or adversary's turn it is
  - This is done by index (players are 0 - players.size() - 1, adversaries are players.size() - players.size()+ adversaries.size() - 1)
  - When the integer exceeds the total number of players and adversaries, it is reset to 0
- List<Adversary<Adversary>> adversaries
  - This list represents all the adversaries in the game
  
<br>
With that data in mind, it's planned interface will contain the following methods:
<br>

- void addPlayer(String name)
  - This method takes in the name of the new player and adds it to the list of players
- void startGame(Level level)
  - This method starts the game by initializing the GameState
  - This entails setting the level and creating Player objects to represent each User object
- void promptPlayerTurn()
  - This method prompts the correct Actor to take a turn by calling the correct User's turn method
  - user.turn() returns an Action, which is then passed into executeAction
  - After the action is executed, each User is given updated information on the GameState using the method updatePlayerView()
    - An example of this is if one User moves into other Users' visible tiles, than we know that their visible tiles must be updated
  - The turn int is upped after every call
  - If it is an Adversary's turn, it's automated turn code is called.
- void executeAction(Action action)
  - This method executes the given Action on the GameState
- Level generateRandomLevel()
  - This method generates a random level
  - A possible extension of this would be to have a parameter difficultySeed that dictates how difficult the generated level should be.
<br>
Please let us know if you have questions about the contents of this memo.
<br>
Best regards,
<br>
  Megan Larson & Jalaj Signh
