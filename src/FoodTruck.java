import java.util.Calendar;

public class FoodTruck {
    String Name;
    String Address;
    String start24;
    String end24;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStart24() {
        return start24;
    }

    public void setStart24(String start24) {
        this.start24 = start24;
    }

    public String getEnd24() {
        return end24;
    }

    public boolean isOpen() {
        Calendar c = Calendar.getInstance();
        int currHour = c.HOUR_OF_DAY + 1;
        int start = Integer.parseInt(this.start24.split(":")[0]);
        int end = Integer.parseInt(this.end24.split(":")[0]);
        if(start <= currHour && currHour <= end) return true;
        return false;
    }

    public void setEnd24(String end24) {
        this.end24 = end24;
    }
}
