# Traveller Interface

## Data

There are two key classes required for the implementation of the game Traveller. The first of which is the class Character, which represents the characters that can traverse the towns and only has one field, which is a String *name*. The second class is the Town. Each Town has a *name* String, a list of other Town objects *neighbors*, since each town exists in a simple network of towns, and a *character* of the Character type. If the *character* field is Null, the town is unoccupied.

## Operations

Our implementation must be able to support the following operations:

-   Create a new character. Given a String for the *name*, return a Character.

-   Create a new town. Given a String for the *name* and a List<Town> for its neighbors, return a Town.

-   Place a character in a town. Given a Character and a Town, set the given Character as the Town's *character*.

-   Query whether a specified character can reach a designated town without running into any other characters. Given a Character and a Town, return true if an unoccupied path exists between the Town containing the character and the given Town. Return false if there is no unoccupied path or the given Character has not been placed in a town.

## Behavior

With the following data structures and operations in mind, the following behaviors should guide the game play. First, if town A is created with a list of *neighbors* containing towns B and C, B's and C's *neighbors* should now include Town A. Second, a character cannot be placed in a Town that already has a specified *character*. Third, if Character A is placed in a town, the town's *character* must now be set to Character A. Fourth, no character can be created with the same *name* as an existing character. Likewise, no two towns should have the same *name* either. Finally, a town's list of neighbors will never contain itself.


