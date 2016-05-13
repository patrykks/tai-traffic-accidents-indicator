package pl.edu.agh.tai.web.bing.map.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONIterator implements Iterator<JSONObject> {

    private JSONArray array;
    private int length;
    private int pointer;

    public JSONIterator(JSONArray array) {
        this.array = array;
        this.length = array.length();
        this.pointer = 0;
    }

    public boolean hasNext() {
        return pointer < length;
    }

    public JSONObject next() {
        pointer++;
        return array.getJSONObject(pointer - 1);
    }

    public void remove() {
        array.remove(pointer);
        length--;
    }

}
