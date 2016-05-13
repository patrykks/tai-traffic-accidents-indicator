package pl.edu.agh.tai.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.web.bing.map.TAIMongoClient;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.bing.map.utils.MongoUpdater;

import java.io.IOException;
import java.util.List;

@Repository
public class IncidentDAOImpl implements IncidentDAO {

    @Autowired
    private TAIMongoClient mongoClient;

    @Autowired
    private MongoUpdater mongoUpdater;

    @Override
    public List<IncidentItem> getAllIncidents() {
        return mongoClient.findAll(IncidentItem.class);
    }

    @Override
    public List<IncidentItem> getAllIncidentsFromArea(GeoJsonPoint point, double radious) throws IOException {
        return mongoClient.getAccidentsInRadius(point, radious);
    }

    @Override
    public List<IncidentItem> getIncidentsFromAreaWithType(GeoJsonPoint point, double radius, List<Type> types, List<Severity> severities) throws IOException {
        return mongoClient.getAccidentsInRadiusWithSeverityAndType(point, radius, severities, types);
    }

    @Scheduled(fixedRate = 300000)
    public void updateDatabaseWithDataFromExternalService() {
        System.out.println("Update database");
        //mongoUpdater.update();
        System.out.println("End update database");
    }

}
