package pl.edu.agh.tai.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import pl.edu.agh.tai.model.bing.traffic.TAIRequest;
import pl.edu.agh.tai.model.bing.traffic.TAIResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

public class MongoDBUpdater {
    @Autowired
    private IncidentDAO dao;
    @Autowired
    private Environment env;
    @Autowired
    private MongoOperations mongoOperations;

    public MongoDBUpdater() {
    }

    public void update() {
        TreeMap<Long, DBObject> outMap = dao.getAllBingIncidentsAsMap();
        System.out.println(new Timestamp(new Date().getTime()) + ": Fetched from Mongo.");

        TreeMap<Long, DBObject> inMap = new TreeMap<>();
        TAIResponse response = fetchBingIncidents();
        if (response != null) {
            Iterator<JSONObject> jsonIterator = response.iterator();
            while (jsonIterator.hasNext()) {
                DBObject incidentItem = (DBObject) JSON.parse(jsonIterator.next().toString());
                Long id = customizeIncident(incidentItem);
                inMap.put(id, incidentItem);
            }
        }
        System.out.println(new Timestamp(new Date().getTime()) + ": Fetched from Bing.");

        Iterator<Entry<Long, DBObject>> outIt = outMap.entrySet().iterator();
        Iterator<Entry<Long, DBObject>> inIt = inMap.entrySet().iterator();
        if (outIt.hasNext() && inIt.hasNext()) {
            Entry<Long, DBObject> out = outIt.next();
            Entry<Long, DBObject> in = inIt.next();
            while (true) {
                if (out.getKey().equals(in.getKey())) {
                    if (compareIncidents(out.getValue(), in.getValue()))
                        inIt.remove();
                    outIt.remove();
                    if (!outIt.hasNext() || !inIt.hasNext()) break;
                    out = outIt.next();
                    in = inIt.next();
                } else if (out.getKey() < in.getKey()) {
                    if (!outIt.hasNext()) break;
                    out = outIt.next();
                } else {
                    if (!inIt.hasNext()) break;
                    in = inIt.next();
                }
            }
        }
        System.out.println(new Timestamp(new Date().getTime()) + ": Filtered incidents.");

        removeIncidents(new ArrayList<>(outMap.keySet()));
        System.out.println(new Timestamp(new Date().getTime()) + ": Removed " + outMap.size() + " incidents.");
        updateIncidents(inMap.values());
        System.out.println(new Timestamp(new Date().getTime()) + ": Added " + inMap.size() + " incidents.");
        dao.createIndexes();
    }

    private TAIResponse fetchBingIncidents() {
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(new TAIRequest().setBounds(49, 14, 55, 24).build()).build();
            return new TAIResponse(httpClient.newCall(request).execute().body().string());
        } catch (IOException e) {
            System.out.println("Exception during getting response to update database");
        }
        return null;
    }

    private Long customizeIncident(DBObject incidentItem) {
        incidentItem.removeField("__type");
        Long id = (Long) incidentItem.removeField("incidentId");
        incidentItem.put("_id", id);
        return id;
    }

    private boolean compareIncidents(DBObject out, DBObject in) {
        String date = (String) in.get("lastModified");
        in.put("lastModified", out.get("lastModified"));
        boolean equal = out.equals(in);
        in.put("lastModified", date);
        return equal;
    }

    private void removeIncidents(List<Long> ids) {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        bingIncidentsCollection.remove(new BasicDBObject("_id", new BasicDBObject("$in", ids)));

        DBCollection ourIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        ourIncidentsCollection.remove(new BasicDBObject("end", new BasicDBObject("$lt", System.currentTimeMillis() + 86400)));
    }

    private void updateIncidents(Collection<DBObject> incidents) {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        incidents.forEach(bingIncidentsCollection::save);
    }
}