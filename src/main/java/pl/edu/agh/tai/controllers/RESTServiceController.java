package pl.edu.agh.tai.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;
import pl.edu.agh.tai.dao.IncidentDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RESTServiceController {

    @Autowired
    private IncidentDAO dao;

    @RequestMapping(value = "/map/accidents", method = RequestMethod.GET)
    public String getTrafficIncidents() {
        return dao.getAllIncidents();
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
            @RequestParam(value = "severity", required = false) List<Integer> sevs,
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

    @RequestMapping(value = "/map/accidents/add", method = RequestMethod.POST)
    public String update(@RequestBody String jsonIncidentItem) {
        BasicDBObject object = (BasicDBObject) JSON.parse(jsonIncidentItem);
        if (object != null) {
            if (object.get("_id") == null) {
                object.put("creator", getCurrentUser());
            }
            return dao.saveOrUpdate(object);
        }
        return null;
    }

    @RequestMapping(value = "/map/accidents/remove", method = RequestMethod.POST)
    public ResponseEntity remove(@RequestBody String id) {
        dao.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/vote")
    public Boolean vote(@RequestParam(value = "incident") String incident,
                        @RequestParam(value = "votes") Integer votes) {
        return dao.vote(incident, votes, getCurrentUser());
    }

    @RequestMapping(value = "/enums/severity")
    public List<String> getSeverity() {
        return Arrays.asList(Severity.values()).stream().map(Enum::name).collect(Collectors.toList());
    }

    @RequestMapping(value = "/enums/type")
    public List<String> getType() {
        return Arrays.asList(Type.values()).stream().map(Enum::name).collect(Collectors.toList());
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/admin/user/show", method = RequestMethod.GET)
    public String getUsers() {
        return dao.getUsers();
    }

    @RequestMapping(value = "/admin/user/ban", method = RequestMethod.PUT)
    public ResponseEntity banUser(@RequestBody  String content) {
        JSONObject userBanOperationData = new JSONObject(content);
        Boolean value = userBanOperationData.getBoolean("value");
        String userId = userBanOperationData.getJSONObject("user").getString("_id");
        User user = dao.getUserWithId(userId);
        user.setAccountNonLocked(value);
        dao.saveUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

}
