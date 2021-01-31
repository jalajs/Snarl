
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    try {
      ServerSocket serverSocket = new ServerSocket(8000);
      Socket socket = serverSocket.accept();
      Scanner scanner = new Scanner(socket.getInputStream());
      OutputStream output = socket.getOutputStream();
      PrintWriter clientWriter = new PrintWriter(output, true);

      // operation will always be sum
      String operation = "sum";
      JSONArray res = new JSONArray();

      String tokenerSource = "";
      while (scanner.hasNextLine()) {
        String next = scanner.nextLine();
        if (next.equals("END")) {
          break;
        }
        tokenerSource += next + ' ';
      }

      JSONTokener tokener = new JSONTokener(tokenerSource.trim());

      while (tokener.more()) {
        Object value = tokener.nextValue();
        String valueString = value.toString();
        if (value instanceof String) {
          if (!isListOfNumerics(valueString)) {
            valueString = "\"" + valueString + "\"";
          }
        }
        List<JSONObject> joList = numJSONTotal(valueString, operation);
        for (int i = 0; i < joList.size(); i++) {
          res.put(joList.get(i));
        }
      }

      // write back to the client
      clientWriter.println(res.toString());

      scanner.close();
      socket.close();
      serverSocket.close();

    } catch (IOException e) {
      System.out.print(e);
    }

  }

  // return true if a string describes a seq of numbers delineated by white space
  private static boolean isListOfNumerics(String s) {
    boolean result = true;
    String[] integers = s.split(" ");
    for (String i: integers) {
      if (!isNumeric(i)) {
        result = false;
      }
    }
    return result;
  }

  // determine if a string is numeric
  public static boolean isNumeric(String s) {
    try {
      int i = Integer.parseInt(s);
    } catch (NumberFormatException e){
      return false;
    }
    return true;
  }

  // create list of { "object": … "total": # } based on the value from the JSONtokener
  public static List<JSONObject> numJSONTotal(String s, String operation) {
    List<JSONObject> joList = new ArrayList<>();
    if (isNumeric(s)) {
      JSONObject jo = new JSONObject();
      jo.put("object", Integer.parseInt(s));
      jo.put("total", Integer.parseInt(s));
      joList.add(jo);
    }
    else if (s.charAt(0) == '[') {
      JSONObject jo = new JSONObject();
      int total = getJSONNums(s, operation);
      jo.put("object", new JSONArray(s));
      jo.put("total", total);
      joList.add(jo);
    }
    else if (s.charAt(0) == '{') {
      JSONObject jo = new JSONObject();
      int total = getJSONNums(s, operation);
      jo.put("object", new JSONObject(s));
      jo.put("total", total);
      joList.add(jo);
    }
    else if (s.charAt(0) == '\"') {
      JSONObject jo = new JSONObject();
      jo.put("object", s.replace("\"", ""));
      jo.put("total", 0);
      joList.add(jo);
    }
    else {
      String[] integers = s.split(" ");
      for (String i: integers) {
        if (isNumeric(i)) {
          JSONObject jo = new JSONObject();
          jo.put("object", Integer.parseInt(i));
          jo.put("total", Integer.parseInt(i));
          joList.add(jo);
        }
      }
    }

    return joList;

  }

  // factor in the given int to the total based on the operation
  public static int computeTotal(int total, int parseInt, String operation) {
    if (operation.equals("sum")) {
      return total + parseInt;
    } else  {
      return total * parseInt;
    }
  }

  // gets only the numbers in the NumJSON associated to the key payload
  public static int getJSONNums(String payload, String operation) {
    int total = 0;
    if (operation.equals("product")) {
      total = 1;
    }
    if (isNumeric(payload)) {
      total = computeTotal(total, Integer.parseInt(payload), operation);
    }
    if (payload.charAt(0) == '[') {
      payload = payload.replace("[", "");
      payload = payload.replace("]", "");
      String[] array = payload.split(",");



      for (String str: array) {
        if(isNumeric(str)) {
          total = computeTotal(total, Integer.parseInt(str), operation);
        }
        else {
          total = computeTotal(total, getJSONNums(str, operation), operation);
        }
      }
    }
    if (payload.charAt(0) == '{') {
      JSONObject itemObj = new JSONObject(payload);
      String input = itemObj.get("payload").toString();
      total = computeTotal(total, getJSONNums(input, operation), operation);
    }
    return total;
  }

  private static Object createObject(String str) {
    if (isNumeric(str)) {
      return Integer.parseInt(str);
    }
    if (str.charAt(0) == '[') {
      str = str.replace("[", "");
      str = str.replace("]", "");
      String[] array = str.split(",");
      for (String ele : array) {
        Object obj = createObject(ele);
      }
    }
    if (str.charAt(0) == '{') {
      JSONObject itemObj = new JSONObject(str);
      return itemObj;
    }
    else {
      return str.replace("\"", "");
    }
  }

}