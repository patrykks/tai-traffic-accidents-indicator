package pl.edu.agh.tai.model.bing.traffic;

import org.json.JSONArray;
import org.json.JSONObject;

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


    private class JSONIterator implements Iterator<JSONObject> {

        private JSONArray array;
        private int length;
        private int pointer;

        JSONIterator(JSONArray array) {
            this.array = array;
            this.length = array.length();
            this.pointer = 0;
        }

        public boolean hasNext() {
            return pointer < length;
        }

        public JSONObject next() {
            return array.getJSONObject(pointer++);
        }

        public void remove() {
            array.remove(pointer);
            length--;
        }
    }
}
