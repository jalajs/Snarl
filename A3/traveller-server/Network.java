import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// represents a network of towns connected by paths
public class Network {
  List<Town> towns;
  List<Path> paths;

  // creates an empty network
  public Network() {
    this.towns = new ArrayList<>();
    this.paths = new ArrayList<>();
  }

  // adds an unconnected town to the network
  public void addTown(String name) {
    Town town = new Town(name);
    this.towns.add(town);
  }

  // connects two towns
  public void addPath(String to, String from) {
    Town toTown;
    Town fromTown;
    List<Town> neighbors = new ArrayList<>();

    for(Town t: this.towns) {
      if (t.getName().equals(to)) {
        toTown = t;
        neighbors.add(toTown);
      }
      if (t.getName().equals(from)) {
        fromTown = t;
        neighbors.add(fromTown);
      }
    }
    int id = this.paths.size();
    Path newPath = new Path(id, neighbors);
    this.paths.add(newPath);
  }

  // determines if a character can reach a town without encountering another character
  public boolean isPassageSafe(String characterName, String townName) {
    Town startingPoint = null;

    boolean destinationExist = false;
    for(Town t: this.towns) {
      Person p = t.getCharacter();
      if (p.getName().equals(characterName)) {
        startingPoint = t;
        break;
      }
      if(t.getName().equals(townName)) {
        destinationExist = true;
      }
    }
    if (!destinationExist) {
      throw new IllegalArgumentException("The destination town does not exist");
    }
    if (startingPoint == null) {
      throw new IllegalArgumentException("The character has not been placed in the game yet");
    }

    LinkedList<Town> townQueue = new LinkedList<>();
    List<String> visitedTowns = new ArrayList<>();
    towns.add(startingPoint);

    while(!townQueue.isEmpty()) {
      Town current = townQueue.removeFirst();

      if (visitedTowns.contains(current.getName())) {
        continue;
      }

      visitedTowns.add(current.getName());

      if (current.getName().equals(townName)) {
        return true;
      }

      if(current.isOccupied()) {
        for (Path p: this.paths) {
          List<Town> neighbors = p.getTowns();
          if(neighbors.contains(current)) {
            for (Town t: neighbors) {
              townQueue.add(t);
            }
          }
        }
      }
    }
    return false;
  }

}
