package pl.edu.agh.tai.model.bing.traffic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;

import java.util.List;
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
        DBCollection incidents = mongoOperations.getCollection(env.getProperty("mongodb.incidentsCollection"));

        DBObject query = new BasicDBObject();

        DBObject projection = new BasicDBObject();
        projection.put("point", 1);
        projection.put("description", 1);

        DBCursor result = incidents.find(query, projection);
        return cursorToString(result);
    }

    public String getAccidentsInRadius(GeoJsonPoint point, double radius) {
        return getAccidentsInRadiusWithSeverityAndType(point, radius, null, null);
    }

    public String getAccidentsInRadiusWithSeverityAndType(GeoJsonPoint point, double radius, List<Severity> sevs, List<Type> types) {
        DBCollection incidents = mongoOperations.getCollection(env.getProperty("mongodb.incidentsCollection"));

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

        DBCursor result = incidents.find(query, projection);
        return cursorToString(result);
    }

    public void save(IncidentItem incidentItem) {
        mongoOperations.save (incidentItem);
    }

    private String cursorToString(DBCursor result) {
        StringBuilder incidentArray = new StringBuilder("[");
        while (result.hasNext()) {
            incidentArray.append(result.next().toString()).append(",");
        }
        incidentArray.setCharAt(incidentArray.length() - 1, ']');
        return incidentArray.toString();
    }

}
