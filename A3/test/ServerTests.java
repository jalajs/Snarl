import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerTests {



  @Test
  public void testIsPassageSafe() {
    TravellerNetwork travellerNetwork = new TravellerNetwork();
    travellerNetwork.addTown("Seattle");
    travellerNetwork.addTown("Boston");
    travellerNetwork.addTown("New York");
    travellerNetwork.addTown("Amsterdam");

    travellerNetwork.addPath("Seattle", "Boston");
    travellerNetwork.addPath("Boston", "New York");
    travellerNetwork.addPath("New York", "Amsterdam");

    List<Town> towns = travellerNetwork.getTowns();

    Town seattle = towns.get(0);
    seattle.placeCharacter(new Person(1, "Megan"));

    assertTrue(travellerNetwork.isPassageSafe("Megan", "Amsterdam"));
    assertFalse(travellerNetwork.isPassageSafe("Megan", "Seattle"));


    Town boston = towns.get(1);
    boston.placeCharacter(new Person(2, "Jalaj"));

    assertFalse(travellerNetwork.isPassageSafe("Megan", "Boston"));
    assertFalse(travellerNetwork.isPassageSafe("Jalaj", "Seattle"));
    assertTrue(travellerNetwork.isPassageSafe("Jalaj", "Amsterdam"));

    //boston
    // Seattle ------> Boston ------> New York ----- > Amsterdam






  }
}
