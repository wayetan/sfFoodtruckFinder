import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class FoodTruckFinder {
    static int limit = 10;
    static int offset = 10;
    static String sourceURL = "https://data.sfgov.org/resource/bbb8-hzi6.json";

    private static String getUrlWithPage(int page) {
        int currOffset = 0;
        if(page > 0) currOffset = (page - 1) * offset;
        String urlStr = sourceURL + "?$limit=" + limit + "&$offset=" + currOffset +"&dayofweekstr=";
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        switch (day_of_week) {
            case Calendar.SUNDAY:
                urlStr += "Sunday";
                break;
            case Calendar.MONDAY:
                urlStr += "Monday";
                break;
            case Calendar.TUESDAY:
                urlStr += "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                urlStr += "Wednesday";
                break;
            case Calendar.THURSDAY:
                urlStr += "Thursday";
                break;
            case Calendar.FRIDAY:
                urlStr += "Friday";
                break;
        }
        return urlStr;
    }

    private static String getContent(String urlStr) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result.toString();
    }

    public static void promptEnterKey() {
        try {
            System.out.println("Press \"ENTER\" to see next 10 results...");
            System.in.read();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<FoodTruck> parseJSON(String jsonStr) {
        List<FoodTruck> trucks = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonStr);
            Iterator<JsonNode> childIterator = root.elements();
            while (childIterator.hasNext()) {
                JsonNode node = childIterator.next();
                FoodTruck truck = new FoodTruck();
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
                    if(field.getKey().equals("applicant")) truck.setName(field.getValue().toString());
                    else if(field.getKey().equals("location")) truck.setAddress(field.getValue().toString());
                    else if(field.getKey().equals("start24")) truck.setStart24(field.getValue().toString());
                    else if(field.getKey().equals("end24")) truck.setEnd24(field.getValue().toString());
                }
                trucks.add(truck);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return trucks;
    }
    public static void main(String[] args) {
        int count = 0;
        String url = getUrlWithPage(count);
        String jsonStr = getContent(url);
        List<FoodTruck> trucks = parseJSON(jsonStr);
        Collections.sort(trucks, new Comparator<FoodTruck>() {
            @Override
            public int compare(FoodTruck t1, FoodTruck t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });
        System.out.println("Name: " + "Address: ");
        for(FoodTruck t : trucks) {
            System.out.println(t.getName() + "," + t.getAddress());
        }


    }


}
