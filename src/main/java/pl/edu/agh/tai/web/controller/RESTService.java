package pl.edu.agh.tai.web.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.bing.map.utils.GeoModuleExt;
import pl.edu.agh.tai.web.bing.map.utils.JSONIterator;
import pl.edu.agh.tai.web.bing.map.utils.MicrosoftDateFromat;
import pl.edu.agh.tai.web.dao.IncidentDAO;

@RestController
public class RESTService {
	@Autowired
	private IncidentDAO dao;

	@Autowired
	@Qualifier("defaultObjectMapper")
	private ObjectMapper defaultObjectMapper;

	@Autowired
	@Qualifier("geoJsonObjectMapper")
	private ObjectMapper geoObjectMapper;

	@RequestMapping(value = "/map/accidents")
	public String getTrafficIncidents() {
		List<IncidentItem> incidents = dao.getAllIncidents();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			defaultObjectMapper.writeValue(out, incidents);
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
			incidents.addAll(dao.getAllIncidentsFromArea(new GeoJsonPoint(50.607392, 15.83),10));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			geoObjectMapper.writeValue(out, incidents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final byte[] data = out.toByteArray();
		return new String(data);
	}

	@RequestMapping(value = "/map/accidents/test/geowithtype")
	public String getTrafficIncidentsFromAreaWithType() {
		List<IncidentItem> incidents = new ArrayList<IncidentItem>();
		try {
			//getAccidentsInRadiusWithSeverityAndType(new GeoJsonPoint(50.607392, 15.83),10, Arrays.asList(Severity.valueOf("SERIOUS")), Arrays.asList())
			incidents.addAll(dao.getIncidentsFromAreaWithType(new GeoJsonPoint(50.607392, 15.83),10, Arrays.asList(), Arrays.asList(Severity.valueOf("MINOR"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			geoObjectMapper.writeValue(out, incidents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final byte[] data = out.toByteArray();
		return new String(data);
	}

/*


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView method() {
		return new ModelAndView("redirect:" + "/static/home.html");

	}

*/

}
