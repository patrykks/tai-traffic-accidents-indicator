package pl.edu.agh.tai.web.controller;

import java.io.*;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.tai.web.bing.map.model.IncidentItem;
import pl.edu.agh.tai.web.bing.map.utils.GeoModuleExt;
import pl.edu.agh.tai.web.bing.map.utils.JSONIterator;
import pl.edu.agh.tai.web.bing.map.utils.MicrosoftDateFromat;
import pl.edu.agh.tai.web.dao.IncidentDAO;

@RestController
public class AjaxController {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MongoOperations mongoOperation;

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




}
