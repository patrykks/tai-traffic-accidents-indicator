package pl.edu.agh.tai.model.bing.traffic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class TAIMongoClient {

    @Autowired
    private Environment env;

    @Autowired
    private MongoOperations mongoOperations;

    public TAIMongoClient() {
    }

    public String findAll() {
        return fetchFromBothCollections(new BasicDBObject(), new BasicDBObject());
    }

    public String getAccidentsInRadius(GeoJsonPoint point, double radius) {
        return getAccidentsInRadiusWithSeverityAndType(point, radius, null, null);
    }

    public String getAccidentsInRadiusWithSeverityAndType(GeoJsonPoint point, double radius, List<Severity> sevs, List<Type> types) {
        List<Double> invertedCoordinates = point.getCoordinates().subList(0, point.getCoordinates().size());
        DBObject geometry = new BasicDBObject("type", "Point").append("coordinates", invertedCoordinates);
        DBObject nearSphere = new BasicDBObject("$geometry", geometry).append("$maxDistance", radius);
        BasicDBObject query = new BasicDBObject("point", new BasicDBObject("$nearSphere", nearSphere));
        if (sevs != null && !sevs.isEmpty())
            query.append("severity", new BasicDBObject("$in", sevs.stream().map(Severity::toString).collect(Collectors.toList())));
        if (types != null && !types.isEmpty())
            query.append("severity", new BasicDBObject("$in", types.stream().map(Type::toString).collect(Collectors.toList())));

        DBObject projection = new BasicDBObject();
        projection.put("point", 1);
        projection.put("description", 1);

        return fetchFromBothCollections(query, projection);
    }

    public TreeMap<Long, DBObject> getAllBingIncidentsAsMap() {
        DBCollection incidents = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        DBCursor result = incidents.find();

        TreeMap<Long, DBObject> map = new TreeMap<>();
        while (result.hasNext()) {
            DBObject item = result.next();
            map.put((Long) item.get("_id"), item);
        }
        return map;
    }
    @SuppressWarnings("unchecked")
    public Boolean vote(String id, int points, String user) {
        DBCollection incidents = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        DBCursor result = incidents.find(new BasicDBObject("_id", new ObjectId(id)));
        if(result.hasNext()) {
            DBObject incident = result.next();

            List<String> voters = (List<String>) incident.get("voters");
            if(!voters.contains(user)) {
                incident.put("votes", (Integer) incident.get("votes") + points);
                voters.add(user);
                return incidents.save(incident).wasAcknowledged();
            }
        }
        return false;
    }

    public void remove(String id) {
        DBCollection incidents = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        incidents.remove(new BasicDBObject("_id", new ObjectId(id)));
    }

    public String save(BasicDBObject incident) {
        DBCollection ourIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        ourIncidentsCollection.save(incident);
        return incident.getObjectId("_id").toString();
    }

    public void createIndexes() {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        bingIncidentsCollection.createIndex(new BasicDBObject("point", "2dsphere"));
        DBCollection ourIncidentsCollection = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        ourIncidentsCollection.createIndex(new BasicDBObject("point", "2dsphere"));
    }

    private String fetchFromBothCollections(DBObject query, DBObject projection) {
        DBCollection bingIncidents = mongoOperations.getCollection(env.getProperty("mongodb.bingIncidentsCollection"));
        String bingResult = cursorToString(bingIncidents.find(query, projection));
        DBCollection taiIncidents = mongoOperations.getCollection(env.getProperty("mongodb.ourIncidentsCollection"));
        String taiResult = cursorToString(taiIncidents.find(query, projection));

        return "{ " + "\"bing\" : " + bingResult + " , " + "\"tai\" : " + taiResult + " }";

    }

    private String cursorToString(DBCursor result) {
        StringBuilder incidentArray = new StringBuilder("[");
        while (result.hasNext()) {
            incidentArray.append(result.next().toString()).append(",");
        }
        if(incidentArray.length() > 1)
            incidentArray.setCharAt(incidentArray.length() - 1, ']');
        else
            incidentArray.append("]");
        return incidentArray.toString();
    }

    public User getUserWithId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return  mongoOperations.findOne(query, User.class);
    }

    public void saveUser(User user) {
        mongoOperations.save(user);
    }

    public String getUsers() {
        DBCollection users = mongoOperations.getCollection(env.getProperty("mongodb.users"));
        DBCursor cursor = users.find();
        return cursorToString(cursor);
    }
}
