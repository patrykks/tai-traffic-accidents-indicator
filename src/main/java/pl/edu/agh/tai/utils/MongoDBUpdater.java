package pl.edu.agh.tai.utils;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.tai.dao.IncidentDAO;
import pl.edu.agh.tai.model.bing.traffic.TAIResponse;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

public class MongoDBUpdater {
    private static Logger logger = LoggerFactory.getLogger(MongoDBUpdater.class);

    @Autowired
    private IncidentDAO dao;
    @Autowired
    IncidentManager incidentManager;

    public MongoDBUpdater() {
    }

    public void update() {
        TreeMap<Long, DBObject> outMap = dao.getAllBingIncidentsAsMap();
        System.out.println(new Timestamp(new Date().getTime()) + ": Fetched from Mongo.");

        TreeMap<Long, DBObject> inMap = new TreeMap<>();
        TAIResponse response = incidentManager.fetchBingIncidents();
        if (response != null) {
            Iterator<JSONObject> jsonIterator = response.iterator();
            while (jsonIterator.hasNext()) {
                DBObject incidentItem = (DBObject) JSON.parse(jsonIterator.next().toString());
                Long id = incidentManager.customizeIncident(incidentItem);
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
                    if (incidentManager.compareIncidents(out.getValue(), in.getValue()))
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

        incidentManager.removeIncidents(new ArrayList<>(outMap.keySet()));
        System.out.println(new Timestamp(new Date().getTime()) + ": Removed " + outMap.size() + " incidents.");
        incidentManager.updateIncidents(inMap.values());
        System.out.println(new Timestamp(new Date().getTime()) + ": Added " + inMap.size() + " incidents.");
        dao.createIndexes();
    }
}

/*
2016-06-03 11:51:51.892: Update database

On start:
    BING incidents: 847
    Our incidents: 858

Incidents missing in BING DB (outdated ones): 13
Incidents missing in Our DB (new ones): 1
Incidents present in both DB (current ones): 846
    Completely equal: 49
    Really modified: 1
    Equal but lastModified field: 796

Results:
    removed: 13
2016-06-03 11:51:58.544: Removed incidents.

    inserted: 798
2016-06-03 11:56:56.845: Added incidents.

2016-06-03 11:56:57.143: End update database
*/