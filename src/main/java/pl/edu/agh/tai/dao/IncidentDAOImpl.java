package pl.edu.agh.tai.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import pl.edu.agh.tai.model.bing.traffic.TAIMongoClient;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.model.enums.SignInProvider;
import pl.edu.agh.tai.utils.MongoDBUpdater;

import java.util.List;

@Repository
public class IncidentDAOImpl implements IncidentDAO {

    @Autowired
    private TAIMongoClient mongoClient;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MongoDBUpdater mongoDBUpdater;

    @Override
    public String getAllIncidents() {
        return mongoClient.findAll();
    }

    @Override
    public String getAllIncidentsFromArea(GeoJsonPoint point, double radious) {
        return mongoClient.getAccidentsInRadius(point, radious);
    }

    @Override
    public String getIncidentsFromAreaWithType(GeoJsonPoint point, double radius, List<Type> types, List<Severity> severities) {
        return mongoClient.getAccidentsInRadiusWithSeverityAndType(point, radius, severities, types);
    }

    @Override
    public void saveOrUpdate(IncidentItem incidentItem) {
        mongoClient.save(incidentItem);
    }


    @Scheduled(fixedRate = 300000)
    public void updateDatabaseWithDataFromExternalService() {
        System.out.println("Update database");
        //mongoDBUpdater.update();
        System.out.println("End update database");
        //createTestUser();
    }

    public void createTestUser() {
        User user = new User("patrykks","patrykks","patryks@gmaikl.com", "demo","patryk","skalski", SignInProvider.TAI);
        user.setRole(1);
        template.save(user);
        User user1 = new User("wojti","wojti","wojteks@gmaikl.com","demo","wojti","mojti", SignInProvider.TAI);
        user1.setRole(2);
        template.save(user1);
        System.out.println("User creating end");
    }

}

