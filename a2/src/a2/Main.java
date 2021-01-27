package a2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
            JSONObject jo = new JSONObject();
            int total = 0;
            if (isNumeric(item)) {
                total = Integer.parseInt(item);
                jo.put("object", total);
            }
            if ( ) {

                String[] array = new String[];
                for (String s: array) {
                    if(isNumeric(s)) {
                        total += Integer.parseInt(s);
                    }
                }
                jo.put("object", array); // => item needs to be right type
            }
            if ( ) {
                JSONObject itemObj = new JSONObject(item);
                jo.put("object", total); // => item needs to be right type

            } else {
                jo.put("object", item);
            }
           // jo.put("object", item); // => item needs to be right type

            // set item as object in return json

            jo.put("total", total);
            // we either sum or product the numbers (must find numbers!)
            // add to accumulator
            res.add(jo);
        }
        scanner.close();

    }

    private static boolean isNumeric(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
