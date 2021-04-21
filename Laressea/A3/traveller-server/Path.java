import java.util.List;

// represents a path between two towns
public class Path {
  int Id;
  boolean isOccupied;
  List<Town> towns;

  // given an id and a list of towns, create a Path
  public Path(int id, List<Town> towns) {
    this.Id = id;
    this.isOccupied = false;
    this.towns = towns;
  }

  public int getId() {
    return Id;
  }

  public void setId(int id) {
    Id = id;
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(boolean occupied) {
    isOccupied = occupied;
  }

  public List<Town> getTowns() {
    return towns;
  }

  public void setTowns(List<Town> towns) {
    this.towns = towns;
  }
}
