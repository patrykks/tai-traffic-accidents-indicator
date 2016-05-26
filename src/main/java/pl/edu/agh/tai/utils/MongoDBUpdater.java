package pl.edu.agh.tai.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import pl.edu.agh.tai.bing.traffic.TAIRequest;
import pl.edu.agh.tai.bing.traffic.TAIResponse;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.IncidentItem;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MongoDBUpdater {
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ObjectMapper objectMapper;


    public MongoDBUpdater() {
    }

    public void update() {
        //deleteOutDateIncidents();
        TAIResponse response = null;
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(new TAIRequest().setBounds(49, 14, 55, 24).addSeverity(Severity.SERIOUS).addSeverity(Severity.MINOR).build()).build();
            response = new TAIResponse(httpClient.newCall(request).execute().body().string());
        } catch (IOException e) {
            System.out.println("Exception druign getting response to update database");
        }
        if (response != null) {
            Iterator<JSONObject> jsonIterator = response.iterator();
            while (jsonIterator.hasNext()) {
                IncidentItem incidentItem = null;
                try {
                    incidentItem = objectMapper.readValue(jsonIterator.next().toString(), IncidentItem.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (incidentItem != null) {
                    mongoOperations.save(incidentItem);
                }
            }
        }
    }

    private void deleteOutDateIncidents() {
        List<IncidentItem> incidents = mongoOperations.findAll(IncidentItem.class);
        incidents.stream().filter(incidentItem -> incidentItem.getEnd().after(new Date()))
                .forEach(incidentItem -> mongoOperations.remove(incidentItem));
    }

}
