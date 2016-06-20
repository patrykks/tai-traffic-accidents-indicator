package pl.edu.agh.tai.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import pl.edu.agh.tai.model.User;

import java.util.List;
import java.util.TreeMap;

public interface IncidentDAO {
    String getAllIncidents();

    TreeMap<Long, DBObject> getAllBingIncidentsAsMap();

    String getAllIncidentsFromArea(GeoJsonPoint point, double radius);

    String getIncidentsFromAreaWithType(GeoJsonPoint point, double radius, List<Integer> types, List<Integer> severities);

    String saveOrUpdate(BasicDBObject incident);

    void remove(String id);

    Boolean vote(String id, int points, String user);

    void createIndexes();

    String getUsers();

    void saveUser(User user);

    User getUserWithId(String id);
}

