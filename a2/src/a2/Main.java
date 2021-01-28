package a2;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Scanner;

public class Main {

    // input: 12 [2, "foo", 4]  { "name" : "SwDev", "payload" : [12, 33], "other" : { "payload" : [ 4, 7 ] } }
    // output --sum:
    //     [ { "object" : 12, "total" : 12 },
    //       { "object" : [2, "foo", 4], "total" : 6 },
    //       { "object" : { "name" : "SwDev", "payload" : [12, 33], "other" : { "payload" : [4, 7] } }, "total" : 45 } ]
    public static void main(String[] args) {
        // parse out first arg
        String operation = args[0];
        System.out.print(operation);

        // write your code here
        JSONArray res = new JSONArray();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String item = scanner.next();
            System.out.print(item);
            JSONObject jo = numJSONTotal(item, operation);
            res.put(jo);
            System.out.print(jo.toString());
        }
        scanner.close();
        System.out.print(res.toString());

    }

    public static boolean isNumeric(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    // create { "object": … "total": # }
    public static JSONObject numJSONTotal(String s, String operation) {
        JSONObject jo = new JSONObject();
        if (isNumeric(s)) {
            jo.put("object", Integer.parseInt(s));
        }
        if (s.charAt(0) == '[') {
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] array = s.split(",");
            jo.put("object", array);
        }
        if (s.charAt(0) == '{') {
            jo.put("object", new JSONObject(s));
        }

        jo.put("total", getJSONNums(s, operation));
        return jo;

    }

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

}
