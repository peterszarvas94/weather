package weather;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData {
    
    private final JSONObject current;
    private final JSONArray hourly;
    private final JSONArray daily;
    
    public WeatherData(JSONObject data) {
        current = data.getJSONObject("current");
        hourly = data.getJSONArray("hourly");
        daily = data.getJSONArray("daily");
    }
    
    public JSONObject getCurrent() {
        return this.current;
    }
    
    public JSONArray getHourly() {
        return this.hourly;
    }
    
    public JSONArray getDaily() {
        return this.daily;
    }
    
}
