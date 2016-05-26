package pl.edu.agh.tai.web.dao;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;

import java.io.IOException;
import java.util.List;

public interface IncidentDAO {
    String getAllIncidents();

    String getAllIncidentsFromArea(GeoJsonPoint point, double radious);

    String  getIncidentsFromAreaWithType(GeoJsonPoint point, double radious, List<Type> types, List<Severity> severities);

}
