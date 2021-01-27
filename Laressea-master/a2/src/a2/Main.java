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
        //rem

	      // write your code here
        JSONArray res = new JSONArray();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String item = scanner.next();
            JSONObject jo = numJSONTotal(item, operation);
            res.put(jo);
        }
        scanner.close();
        System.out.print(res.toString());

    }

    private static boolean isNumeric(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    // create { "object": … "total": # }
    private static JSONObject numJSONTotal(String s, String operation) {
        JSONObject jo = new JSONObject();
        jo.put("object", s);
        jo.put("total", getJSONNums(s, operation));
        return jo;

    }


        // jo.put("object", item); // => item needs to be right type

        // set item as object in return json


        // we either sum or product the numbers (must find numbers!)
        // add to accumulator

    private static int computeTotal(int total, int parseInt, String operation) {
        if (operation.equals("sum")) {
            return total + parseInt;
        } else  {
            return total * parseInt;
        }
    }

    // gets only the numbers in the NumJSON associated to the key payload
    private static int getJSONNums(String payload, String operation) {
        int total = 0;
        if (isNumeric(payload)) {
            total = computeTotal(total, Integer.parseInt(payload), operation);
        }
        if (payload.charAt(0) == '[') {
            // need to remove first and last chars and split on commas
            payload.replace("[", "");
            String[] array = payload.split(", ");

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
        else {
           if (operation.equals("product")) {
               total = 1;
           }
        }

        return total;
        }

}
