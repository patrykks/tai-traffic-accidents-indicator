package pl.edu.agh.tai.web.bing.map.core;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.tai.web.bing.map.utils.JSONIterator;


import java.util.Iterator;

public class TAIResponse {

    private JSONObject json;
    private int totalResources;
    private JSONArray resources;

    public TAIResponse(String response) {
        json = new JSONObject(response);
        //System.out.println(json.toString(4));

        JSONObject resourceSets  = json.getJSONArray("resourceSets").getJSONObject(0);
        totalResources = resourceSets.getInt("estimatedTotal");
        resources  = resourceSets.getJSONArray("resources");
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
