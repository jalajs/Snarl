## Hit Point and Combat System: Give players a fighting chance (or something hoaky this ??)
- In order to accommodate a hit point and combat system, we modified our Action/interaction system and added fields to player and adversary data structures
  - In players, we added a field that kept track of hit points. We decided every player should have 100 hit points start.
    - RemoteUsers and LocalUsers were given this field as well.
  - In adversaries, we gave them a field that keeps track of the damage they can inflict. Zombies are able to inflict 25 points of damage, while the more powerful Ghosts can inflict 50 points of damage.
    - SnarlAdversaries were also give a damage field. Any move action they perform is already loaded with their damage.
- A new interactionType was added called ATTACK.
    - ATTACK describes a move where an adversary comes in contact with a player. This can either be through an adversary or player movement.
    - When our game state handles a move containing ATTACK, it inflicts the damage upon the player and decides whether or not to EJECT. the player 
- MoveAction, our action data structure that contains all relevant information about a move, was expanded to include an optional field for damage and victim name
- It is important to note that adversaries can attack players and cause damage to their hit points, but players cannot attack/harm adversaries. If a player moves to an adversary, instead of being instantly ejected, they lose however many hit points according to the damagePoints of the type of adversary.
- Players will be ejected as soon as their hit points total reaches zero.
- Active players hitpoints are reset at the end of each level
- We added a hitpoints field ({... "hitpoints" : (int) ...}) in the player-update message object in the SNARL protocol

