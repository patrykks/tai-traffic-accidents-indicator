package pl.edu.agh.tai.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.model.IncidentItem;
import pl.edu.agh.tai.dao.IncidentDAO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RESTServiceController {
    @Autowired
    private IncidentDAO dao;

    @Autowired
    private ObjectMapper objectMapper;

    private static int attempt = 0;

    @RequestMapping(value = "/map/accidents", method = RequestMethod.GET)
    public String getTrafficIncidents() {
        if (attempt > 1)
            return dao.getAllIncidents();
        if (attempt++ > 0)
            return dao.getAllIncidentsFromArea(new GeoJsonPoint(50.607392, 15.83), 10);
        return "";
    }

    @RequestMapping(value = "/map/accidents", method = RequestMethod.POST)
    public ResponseEntity<IncidentItem> update(@RequestBody String jsonIncidentItem) {
        IncidentItem incidentItem = null;
        try {
            incidentItem = objectMapper.readValue(jsonIncidentItem, IncidentItem.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (incidentItem != null) {
            dao.saveOrUpdate(incidentItem);
        }
        return new ResponseEntity<IncidentItem>(incidentItem, HttpStatus.OK);
    }

    @RequestMapping(value = "/map/accidents/geo", method = RequestMethod.GET)
    public String getTrafficIncidentsFromArea() {
        return dao.getAllIncidentsFromArea(new GeoJsonPoint(50.607392, 15.83), 10);
    }

    @RequestMapping(value = "/map/accidents/geowithtype", method = RequestMethod.GET)
    public String getTrafficIncidentsFromAreaWithType() {
        //getAccidentsInRadiusWithSeverityAndType(new GeoJsonPoint(50.607392, 15.83),10, Arrays.asList(Severity.valueOf("SERIOUS")), Arrays.asList())
        return dao.getIncidentsFromAreaWithType(new GeoJsonPoint(50.607392, 15.83), 10, Arrays.asList(), Arrays.asList());
    }

    @RequestMapping(value = "/map/accidents/geowithparams", method = RequestMethod.GET)
    public String getTrafficIncidentsFromAreaWithParams(
            @RequestParam(value = "lat") Double lat,
            @RequestParam(value = "lon") Double lon,
            @RequestParam(value = "radius") Double radius,
            @RequestParam(value = "severity", required = false) List<Integer>  sevs,
            @RequestParam(value = "type", required = false) List<Integer> types
    ) {
        List<Severity> sevList = new ArrayList<>();
        if (sevs != null && !sevs.isEmpty())
            sevList = sevs.stream().map(Severity::fromValue).collect(Collectors.toList());
        List<Type> typeList = new ArrayList<>();
        if (types != null && !types.isEmpty())
            typeList = types.stream().map(Type::fromValue).collect(Collectors.toList());
        return dao.getIncidentsFromAreaWithType(new GeoJsonPoint(lat, lon), radius, typeList, sevList);
    }
}
