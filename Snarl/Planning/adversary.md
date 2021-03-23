The following memo contains a description of a SnarlAdversary, which represents the logics behind an adversary. A SnarlAdversary will need the following fields:
<br>
- Level level
    - An adversary gets the full level information (comprised of rooms, hallways and objects) at the beginning of a level
    - This is subject to change if the adversaries are too powerful. Instead of the full level information, adversaries could be given a smaller subset of the level or a tile grid for its surroundings like users.
- Posn adversaryPosn
    - The adversary's current location in relation to the level's origin
<br>
With that data in mind, it's planned interface will contain the following method:
<br>

- Action turn(Map<Posn, Player> players, Map<Posn, Adversary> adversary)
    - Returns action to gameManager
    - Given the mapped locations of players and the adversaries so that the SnarlAdversary can make informed decisions about its next move
      - This is subject to change if it is determined that giving the adversaries this much information makes them too powerful. Instead of exact locations of players, we could give just the rooms each player is in or a general location. 
    - Action is an interface which represents an Actor's move / interaction
        - Action is initially described in our Player/User specification (see player.md)
        - We plan on modifying/extending our Action interface to support actions for players AND adversaries (i.e. actors)
            - We would only need to add an action to support expulsion at the moment. Any other potential future adversary actions could be easily added as well.
<br>
