package pl.edu.agh.tai.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.model.bing.traffic.TAIRequest;
import pl.edu.agh.tai.model.bing.traffic.TAIResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class IncidentManager {
    private static Logger logger = LoggerFactory.getLogger(IncidentManager.class);

    @Autowired
    private Environment env;

    @Autowired
    private MongoOperations mongoOperations;

    public TAIResponse fetchBingIncidents() {
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(new TAIRequest().setBounds(49, 14, 55, 24).build()).build();
            return new TAIResponse(httpClient.newCall(request).execute().body().string());
        } catch (IOException e) {
            System.out.println("Exception during getting response to update database");
        }
        return null;
    }

    public Long customizeIncident(DBObject incidentItem) {
        incidentItem.removeField("__type");
        Long id = (Long) incidentItem.removeField("incidentId");
        incidentItem.put("_id", id);
        return id;
    }

    public boolean compareIncidents(DBObject out, DBObject in) {
        String date = (String) in.get("lastModified");
        in.put("lastModified", out.get("lastModified"));
        boolean equal = out.equals(in);
        in.put("lastModified", date);
        return equal;
    }

    public void removeIncidents(List<Long> ids) {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        bingIncidentsCollection.remove(new BasicDBObject("_id", new BasicDBObject("$in", ids)));

        List<IncidentItem> incidents = mongoOperations.findAll(IncidentItem.class);
        incidents.stream().filter(incidentItem -> new Date().after(incidentItem.getEnd()))
                .forEach(incidentItem -> mongoOperations.remove(incidentItem));
        incidents.stream().map(incidentItem -> incidentItem.getEnd().toString() + " " + new Date().toString()).forEach(logger::debug);
    }

    public void updateIncidents(Collection<DBObject> incidents) {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        incidents.forEach(bingIncidentsCollection::save);
    }
}
