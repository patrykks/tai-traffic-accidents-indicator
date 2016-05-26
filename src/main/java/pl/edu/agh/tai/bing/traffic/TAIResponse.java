package pl.edu.agh.tai.bing.traffic;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.tai.utils.JSONIterator;

import java.util.Iterator;

public class TAIResponse {

    private JSONObject json;
    private int totalResources;
    private JSONArray resources;

    public TAIResponse(String response) {
        json = new JSONObject(response);

        JSONObject resourceSets = json.getJSONArray("resourceSets").getJSONObject(0);
        totalResources = resourceSets.getInt("estimatedTotal");
        resources = resourceSets.getJSONArray("resources");
    }

    public JSONObject getWholeResponse() {
        return json;
    }

    public int getResourcesNumber() {
        return totalResources;
    }

    public Iterator<JSONObject> iterator() {
        return new JSONIterator(resources);
    }

}
