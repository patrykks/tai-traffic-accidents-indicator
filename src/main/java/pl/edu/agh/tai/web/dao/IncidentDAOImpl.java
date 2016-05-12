package pl.edu.agh.tai.web.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.web.bing.map.TAIMongoClient;
import pl.edu.agh.tai.web.bing.map.core.TAIRequest;
import pl.edu.agh.tai.web.bing.map.core.TAIResponse;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.bing.map.utils.MongoUpdater;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 6/05/16.
 */
@Repository
public class IncidentDAOImpl implements IncidentDAO {

    @Autowired
    TAIMongoClient mongoClient;

    @Autowired
    MongoUpdater mongoUpdater;

    @Autowired
    @Qualifier("defaultObjectMapper")
    private ObjectMapper objectMapper;

    @Override
    public List<IncidentItem> getAllIncidents() {
        return mongoClient.findAll(IncidentItem.class);
    }

    @Override
    public List<IncidentItem> getAllIncidentsFromArea(GeoJsonPoint point, double radious) throws IOException {
        return mongoClient.getAccidentsInRadius(point, radious);
    }

    @Override
    public List<IncidentItem> getIncidentsFromAreaWithType(GeoJsonPoint point, double radious, List<Type> types, List<Severity> severities) throws IOException {
        return mongoClient.getAccidentsInRadiusWithSeverityAndType(point, radious, severities, types);
    }


    @Scheduled(fixedRate = 300000)
    public void updateDatabaseWithDataFromExternalService() {
        System.out.println("Update database");
        //mongoUpdater.update();
        System.out.println("End update database");
    }
}
