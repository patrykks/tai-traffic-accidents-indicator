package pl.edu.agh.tai.dao;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;

import java.util.List;

public interface IncidentDAO {
    String getAllIncidents();

    String getAllIncidentsFromArea(GeoJsonPoint point, double radious);

    String  getIncidentsFromAreaWithType(GeoJsonPoint point, double radious, List<Type> types, List<Severity> severities);

    void saveOrUpdate(IncidentItem incidentItem);
}

