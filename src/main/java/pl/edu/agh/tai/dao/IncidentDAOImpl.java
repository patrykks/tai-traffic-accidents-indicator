package pl.edu.agh.tai.dao;

import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.model.bing.traffic.TAIMongoClient;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.utils.MongoDBUpdater;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Repository
public class IncidentDAOImpl implements IncidentDAO {

    @Autowired
    private TAIMongoClient mongoClient;

    @Autowired
    private MongoDBUpdater mongoDBUpdater;

    @Override
    public String getAllIncidents() {
        return mongoClient.findAll();
    }

    @Override
    public TreeMap<Long, DBObject> getAllBingIncidentsAsMap() {
        return mongoClient.getAllBingIncidentsAsMap();
    }

    @Override
    public String getAllIncidentsFromArea(GeoJsonPoint point, double radius) {
        return mongoClient.getAccidentsInRadius(point, radius);
    }

    @Override
    public String getIncidentsFromAreaWithType(GeoJsonPoint point, double radius, List<Type> types, List<Severity> severities) {
        return mongoClient.getAccidentsInRadiusWithSeverityAndType(point, radius, severities, types);
    }

    @Override
    public void saveOrUpdate(IncidentItem incidentItem) {
        mongoClient.saveOrUpdate(incidentItem);
    }

    @Override
    public void remove(String id) {
        mongoClient.remove(id);
    }

    @Override
    public void vote(String id, int points) {
        mongoClient.vote(id, points);
    }

    @Override
    public void createIndexes() {
        mongoClient.createIndexes();
    }


    @Scheduled(fixedRate = 30000000)
    public void updateDatabaseWithDataFromExternalService() {
        System.out.println(new Timestamp(new Date().getTime())  + ": Update database");
        mongoDBUpdater.update();
        System.out.println(new Timestamp(new Date().getTime()) + ": End update database");
    }

}

