package pl.edu.agh.tai.model.bing.traffic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.utils.TAIMongoDBProperties;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class TAIMongoClient {

    @Autowired
    private TAIMongoDBProperties mongoDBProperties;

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
        List invertedCoordinates = point.getCoordinates().subList(0, point.getCoordinates().size());
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
        DBCollection incidents = mongoOperations.getCollection(mongoDBProperties.getProperty("bingIncidentsCollection"));
        DBCursor result = incidents.find();

        TreeMap<Long, DBObject> map = new TreeMap<>();
        while (result.hasNext()) {
            DBObject item = result.next();
            map.put((Long) item.get("_id"), item);
        }
        return map;
    }

    public void vote(String id, int points) {
        DBCollection incidents = mongoOperations.getCollection(mongoDBProperties.getProperty("ourIncidentsCollection"));
        DBCursor result = incidents.find(new BasicDBObject("_id", new ObjectId(id)));
        if(result.hasNext()) {
            DBObject incident = result.next();
            System.out.println(incident);
            incident.put("votes", (Integer) incident.get("votes") + points);
            incidents.save(incident);
        }
    }

    public void remove(String id) {
        DBCollection incidents = mongoOperations.getCollection(mongoDBProperties.getProperty("ourIncidentsCollection"));
        incidents.remove(new BasicDBObject("_id", new ObjectId(id)));
    }

    public void saveOrUpdate(IncidentItem incidentItem) {
        mongoOperations.save(incidentItem);
    }

    public void createIndexes() {
        DBCollection bingIncidentsCollection = mongoOperations.getCollection(mongoDBProperties.getProperty("bingIncidentsCollection"));
        bingIncidentsCollection.createIndex(new BasicDBObject("point", "2dsphere"));
        DBCollection ourIncidentsCollection = mongoOperations.getCollection(mongoDBProperties.getProperty("ourIncidentsCollection"));
        ourIncidentsCollection.createIndex(new BasicDBObject("point", "2dsphere"));
    }

    private String fetchFromBothCollections(DBObject query, DBObject projection) {
        DBCollection bingIncidents = mongoOperations.getCollection(mongoDBProperties.getProperty("bingIncidentsCollection"));
        String bingResult = cursorToString(bingIncidents.find(query, projection));
        DBCollection taiIncidents = mongoOperations.getCollection(mongoDBProperties.getProperty("ourIncidentsCollection"));
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
}
