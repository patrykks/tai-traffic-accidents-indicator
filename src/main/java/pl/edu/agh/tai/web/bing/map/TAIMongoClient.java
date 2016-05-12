package pl.edu.agh.tai.web.bing.map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.bing.map.utils.TAIMongoSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TAIMongoClient {

    @Autowired
    private TAIMongoSettings mongoSettings;

    @Autowired
    private MongoOperations operations;

    public TAIMongoClient() {
    }

    public List<IncidentItem> findAll(Class itemClass) {
        return operations.findAll(IncidentItem.class);
    }

    public List<IncidentItem> getAccidentsInRadius(GeoJsonPoint point, double radius) throws IOException {
        return getAccidentsInRadiusWithSeverityAndType(point, radius, null, null);
    }

    public List<IncidentItem> getAccidentsInRadiusWithSeverityAndType(GeoJsonPoint point, double radius, List<Severity> sevs, List<Type> types) throws IOException {
        DBCollection incidents = operations.getCollection(mongoSettings.getProperty("incidentsCollection"));
        incidents.createIndex(new BasicDBObject("point", "2dsphere"));

        List invertedCoordinates = point.getCoordinates().subList(0, point.getCoordinates().size());

        DBObject geometry = new BasicDBObject("type", "Point").append("coordinates", invertedCoordinates);
        DBObject nearSphere = new BasicDBObject("$geometry", geometry).append("$maxDistance", radius);
        BasicDBObject query = new BasicDBObject("point", new BasicDBObject("$nearSphere", nearSphere));
        if (sevs != null && !sevs.isEmpty())
            query.append("severity", new BasicDBObject("$in", sevs.stream().map(Severity::toString).collect(Collectors.toList())));
        if (types != null && !types.isEmpty())
            query.append("severity", new BasicDBObject("$in", types.stream().map(Type::toString).collect(Collectors.toList())));
        DBCursor result = incidents.find(query);

        return cursorToList(result);
    }

    private List<IncidentItem> cursorToList(DBCursor result) throws IOException {
        List<IncidentItem> incidentItems = new ArrayList<>();
        for (DBObject object : result) {
            IncidentItem item = operations.getConverter().read(IncidentItem.class, object);
            incidentItems.add(item);
        }
        return incidentItems;
    }

}
