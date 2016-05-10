package pl.edu.agh.tai.web.dao;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;

import java.util.List;

/**
 * Created by root on 6/05/16.
 */
public interface IncidentDAO {
    public List<IncidentItem> getAllIncidents();

    public List<IncidentItem> getAllIncidentsFromArea(GeoJsonPoint point, double radious);

    public List<IncidentItem> getIncidentsFromAreaWithType(GeoJsonPoint point, double radious, Type type, Severity severity);
}
