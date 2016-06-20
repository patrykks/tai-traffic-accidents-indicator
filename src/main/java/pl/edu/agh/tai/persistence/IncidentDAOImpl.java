package pl.edu.agh.tai.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.model.enums.SignInProvider;

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
    public String getIncidentsFromAreaWithType(GeoJsonPoint point, double radius, List<Integer> types, List<Integer> severities) {
        return mongoClient.getAccidentsInRadiusWithSeverityAndType(point, radius, severities, types);
    }

    @Override
    public String saveOrUpdate(BasicDBObject incident) {
        return mongoClient.save(incident);
    }

    @Override
    public void remove(String id) {
        mongoClient.remove(id);
    }

    @Override
    public Boolean vote(String id, int points, String user) {
        return mongoClient.vote(id, points, user);
    }

    @Override
    public void createIndexes() {
        mongoClient.createIndexes();
    }

    @Override
    public String getUsers() {
        return mongoClient.getUsers();
    }

    @Override
    public User getUserWithId(String id) { return mongoClient.getUserWithId(id); }


    @Override
    public void saveUser(User user) {
        mongoClient.saveUser(user);
    }

    @Scheduled(fixedRate = 300000)
    public void updateDatabaseWithDataFromExternalService() {
        System.out.println(new Timestamp(new Date().getTime())  + ": Update database");
        mongoDBUpdater.update();
        System.out.println(new Timestamp(new Date().getTime()) + ": End update database");
    }
}

