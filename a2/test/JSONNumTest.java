import org.json.JSONObject;
import org.junit.Test;

import static a2.Main.numJSONTotal;
import static org.junit.Assert.assertEquals;

public class JSONNumTest {

  @Test
  public void testnumJSONTotal() {
    JSONObject res12 = new JSONObject();
    res12.put("object", "12");
    res12.put("total", "12");
    assertEquals(numJSONTotal("12", "sum").get("total"), 12);
    assertEquals(numJSONTotal("[12,12]", "sum").get("total"), 24);
    assertEquals(numJSONTotal("[12,\"foo\"]", "product").get("total"), 12);
    assertEquals(numJSONTotal("{\"name\" : \"SwDev\", \"payload\" :  [12, 33], \"other\" : { \"payload\" : [ 4, 7 ] } }", "sum").get("total"), 45);
    assertEquals(numJSONTotal("{ \"payload\" : { \"payload\" : [2,4] } }", "sum").get("total"), 6);

  }



}
