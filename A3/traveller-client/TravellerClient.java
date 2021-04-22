import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TravellerClient {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    String tokenerSource = "";
    while (scanner.hasNextLine()) {
      String next = scanner.nextLine();
      tokenerSource += next + ' ';
    }

    List<JSONObject> actions = new ArrayList<>();
    JSONTokener tokener = new JSONTokener(tokenerSource.trim());

    while (tokener.more()) {
      Object value = tokener.nextValue();
      JSONObject actionObj = new JSONObject(value.toString());
      actions.add(actionObj);
    }

    try {
      excuteActions(actions);
    } catch (IllegalArgumentException e) {
      System.out.print("Your command cannot be executed with error:"
              + e.getMessage() + " Please try again.");
    }

    scanner.close();
  }


  public static void excuteActions(List<JSONObject> actions) {
    for(int i = 0; i < actions.size(); i++) {
      JSONObject obj = actions.get(i);
      String command =  (String) obj.get("command");
      if(command.equals("roads")){
        if(i != 0) {
          throw new IllegalArgumentException("Roads must be called first and only once.");
        }
        JSONArray params = (JSONArray) obj.get("params");

        for (int j = 0; j < params.length(); j++) {
          JSONObject curObj = (JSONObject) params.get(j);
          String toName = (String) curObj.get("to");
          String fromName = (String) curObj.get("from");

          System.out.print("traveller.checkIfTownExists(to) and traveller.createTown(to) if not");
          System.out.print("traveller.checkIfTownExists(from) and traveller.createTown(from) if not");
          System.out.print("traveller.makeNeighbors(to, from)");
        }
      }
      if(command.equals("place")){
        JSONObject paramObj = (JSONObject) obj.get("params");
        String characterName = (String) paramObj.get("character");
        String townName = (String) paramObj.get("town");
        System.out.print("traveller.placeCharacter(" + characterName  + ", " + townName+ ")");
      }
      if(command.equals("passage-safe?")){
        JSONObject paramObj = (JSONObject) obj.get("params");
        String characterName = (String) paramObj.get("character");
        String townName = (String) paramObj.get("town");
        System.out.print("traveller.passageSafe(" + "characterName" + ", " + townName + ")");
      }
    }
  }
}
