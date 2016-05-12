package pl.edu.agh.tai.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.dao.IncidentDAO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RESTService {
    @Autowired
    private IncidentDAO dao;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/map/accidents")
    public String getTrafficIncidents() {
        List<IncidentItem> incidents = dao.getAllIncidents();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(out, incidents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[] data = out.toByteArray();
        return new String(data);
    }

    @RequestMapping(value = "/map/accidents/test/geo")
    public String getTrafficIncidentsFromArea() {
        List<IncidentItem> incidents = new ArrayList<IncidentItem>();
        try {
            incidents.addAll(dao.getAllIncidentsFromArea(new GeoJsonPoint(50.607392, 15.83), 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return incidentListToString(incidents);
    }

    @RequestMapping(value = "/map/accidents/test/geowithtype")
    public String getTrafficIncidentsFromAreaWithType() {
        List<IncidentItem> incidents = new ArrayList<>();
        try {
            //getAccidentsInRadiusWithSeverityAndType(new GeoJsonPoint(50.607392, 15.83),10, Arrays.asList(Severity.valueOf("SERIOUS")), Arrays.asList())
            incidents.addAll(dao.getIncidentsFromAreaWithType(new GeoJsonPoint(50.607392, 15.83), 10, Arrays.asList(), Arrays.asList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return incidentListToString(incidents);
    }

    @RequestMapping(value = "/map/accidents/test/geowithparams")
    public String getTrafficIncidentsFromAreaWithParams(
            @RequestParam(value = "lat") Double lat,
            @RequestParam(value = "lon") Double lon,
            @RequestParam(value = "radius") Double radius,
            @RequestParam(value = "severity", required = false) List<Integer>  sevs,
            @RequestParam(value = "type", required = false) List<Integer> types
    ) {
        List<IncidentItem> incidents = new ArrayList<>();
        try {
            List<Severity> sevList = new ArrayList<>();
            if (sevs != null && !sevs.isEmpty())
                sevList = sevs.stream().map(Severity::fromValue).collect(Collectors.toList());
            List<Type> typeList = new ArrayList<>();
            if (types != null && !types.isEmpty())
                typeList = types.stream().map(Type::fromValue).collect(Collectors.toList());
            incidents.addAll(dao.getIncidentsFromAreaWithType(new GeoJsonPoint(lat, lon), radius, typeList, sevList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return incidentListToString(incidents);
    }

    private String incidentListToString(List<IncidentItem> incidents) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            objectMapper.writeValue(out, incidents);
            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView method() {
        return new ModelAndView("redirect:" + "/static/index.html");

    }

}
