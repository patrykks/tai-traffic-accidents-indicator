package pl.edu.agh.tai.dao;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.model.IncidentItem;

import java.io.IOException;
import java.util.List;

public interface IncidentDAO {
    public List<IncidentItem> getAllIncidents();

    public List<IncidentItem> getAllIncidentsFromArea(GeoJsonPoint point, double radious) throws IOException;

    public List<IncidentItem> getIncidentsFromAreaWithType(GeoJsonPoint point, double radious, List<Type> types, List<Severity> severities) throws IOException;

}
