import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class FoodTruckFinder {
    // the result size per single fetch
    static int limit = 10;
    // the beginning index of the data
    static int offset = 0;
    static String sourceURL = "https://data.sfgov.org/resource/bbb8-hzi6.json";
    // flag to check if API returns any more data
    static boolean endOfResult = false;

    private static String getUrl() {
        offset = limit + offset;
        String urlStr = sourceURL + "?$limit=" + limit + "&$offset=" + offset +"&dayofweekstr=";
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
            case Calendar.SATURDAY:
                urlStr += "Saturday";
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

    private static List<FoodTruck> parseJSON(String jsonStr) {
        List<FoodTruck> trucks = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonStr);
            Iterator<JsonNode> childIterator = root.elements();
            if(!childIterator.hasNext()) endOfResult = true;
            while (childIterator.hasNext()) {
                JsonNode node = childIterator.next();
                FoodTruck truck = new FoodTruck();
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
                    if(field.getKey().equals("applicant")) truck.setName(field.getValue().toString().replace("\"", ""));
                    else if(field.getKey().equals("location")) truck.setAddress(field.getValue().toString().replace("\"", ""));
                    else if(field.getKey().equals("start24")) truck.setStart24(field.getValue().toString().replace("\"", ""));
                    else if(field.getKey().equals("end24")) truck.setEnd24(field.getValue().toString().replace("\"", ""));
                }
                if(truck.isOpen()) trucks.add(truck);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return trucks;
    }

    private static List<FoodTruck> fetchTrucks() {
        String url = getUrl();
        String jsonStr = getContent(url);
        List<FoodTruck> trucks = parseJSON(jsonStr);
       return trucks;
    }

    private static void fetchNext() {
        try {
            int needed = 10;
            List<FoodTruck> trucks = new ArrayList<>();
            while (trucks.size() != 10 && !endOfResult) {
                List<FoodTruck> fetched = fetchTrucks();
                trucks.addAll(fetched);
                int size = fetched.size();
                needed -= size;
                limit = needed;
            }
            // reset limit to 10
            limit = 10;
            if(trucks.size() > 0) {
                Collections.sort(trucks, new Comparator<FoodTruck>() {
                    @Override
                    public int compare(FoodTruck t1, FoodTruck t2) {
                        return t1.getName().compareTo(t2.getName());
                    }
                });
                System.out.println("Name:                   Address: ");
                for(FoodTruck t : trucks) {
                    System.out.println(t.getName() + ",    " + t.getAddress());
                }
                System.out.println("Press ENTER to see next...");
                System.in.read(new byte[1]);
                fetchNext();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        fetchNext();
    }
}
