Subject: Outline of Milestones in the Development of Snarl <br><br>

Dear Friends at Growl Inc,<br>

   This memo seeks to establish a series of milestones for the process of developing the dungeon crawler Snarl. Each milestone but the first has some element that can feasibly be demoed during a code walk, either through graphics or unit tests. The milestones are as follows: <br><br>

1. Implement the model of the game, including all specified data structures.
2. Establish connection from a client to server and allow for the creation of a user. This step requires a client that allows a user to submit their name. The name must then be sent to the server, who will create a Player object with that name. This is the first ‘demoable’ milestone.
3. Generate the initial information for the game board on the server side. This requires generating the rooms and placing all Collectables, Adversaries, and Player(s).
4. Generate the visible board to the user. This requires sending board information from the server to the client, along with the specific Player’s visible tiles. This step should include the implementation of all general methods required to display graphics.
5. Allow for the user to indicate to the client an action or movement. The client should collect this information from the user and send it to the server.
6. Allow for the server to receive move information from the client and update the board. The updated board information should then be sent back to the client
7. Query third party server at the beginning of the game for adversary code and receive code.
8. Establish connection between multiple clients and the server to allow for multiple Players.
9. Allow for the server to keep track of whose move it is and prompt the correct user or adversary to move. An endless game can now be played.
10. Implement the end game. The game ends either when preset conditions are met. The user can either exit the game, or begin a new one.
<br><br>
These milestones are proposed such that the game will be built from the bottom up, allowing for first the simplest version of the game and then complexifying it with multiple users. Please let the developers know if you have any questions or qualms with the milestones.
<br><br>
Best regards,<br>
	Megan Larson & Jalaj Singh
