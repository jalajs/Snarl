## Adversary Strategies
### Zombies:
- Strategies:
    - Every Zombie has a search radius. If a player is detected within it's search radius, the Zombie moves to the available tile closest to the player.
    - If the player is directly beside (one cardinal move away) the Zombie, the Zombie moves to and ejects the player.
    - If no player is within the radius, the Zombie selects a random move.
- Scenarios:
    - A Zombie is in an area with no players. It moves randomly around the room until a player enters its search radius.
    - A player appears within in the Zombies search radius. The Zombie selects the move closest to the player.
    - A player leaves the Zombie's search radius. The Zombie stops moving towards the player and begins moving randomly again.
    - A player is in a Zombie's search radius but behind a door or otherwise non-Zombie-friendly tile. The Zombie still selects moves closest to the player, leading it to navigate around the obstacle.
    - Two or more players are in a Zombie's search radius. The Zombie moves towards the closest player.
    - A Zombie is chasing a player and the player enters a hallway. The Zombie cannot cross the door, and will not follow the player.
<br> <br>
### Ghosts:
- Strategies
    - Similar to Zombies, Ghosts have a search radius. If a player is within the search radius, Ghosts advance towards the player and avoids wall. Ghosts have a larger search radius than Zombies.
    - If a player is next to a Ghost (one cardinal move away), the Ghost lands on the player.
    - If no player is within the search radius, the Ghost will try to move to a wall. If it is not next to a wall, it will pick a random move. If it is next to a wall, it will pick the wall and be transported.
- Scenarios:
    - A Ghost is in an area with no players, but is next to a wall. The Ghost lands on the wall and is transported.
    - A Ghost is in an area with no players and is not next to a wall. The Ghost picks a random move.
    - A Ghost is near a player. The Ghost moves towards the player. The player moves into a hallway. The Ghost follows the player through the hallway.
    - The player that a Ghost was chasing leaves the search area. The Ghost, who is next to a wall, moves to the wall tile and is transported to another room.  
