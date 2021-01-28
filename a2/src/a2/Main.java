package a2;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        // parse out first arg
        String operation = args[0];
        operation = operation.replace("-", "");

        // write your code here
        JSONArray res = new JSONArray();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String item = scanner.next();
            JSONObject jo = numJSONTotal(item, operation);
            res.put(jo);
        }
        scanner.close();
        System.out.print(res);

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
            Object[] objects = new Object[array.length];
            for (int i = 0; i < objects.length; i++) {
                Object obj = createObject(array[i]);
                objects[i] = obj;
            }
            jo.put("object", objects);
        }
        if (s.charAt(0) == '{') {
            jo.put("object", new JSONObject(s));
        }
        else {
            jo.put("object", s.replace("\"", ""));
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
