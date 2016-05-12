package pl.edu.agh.tai.web.controller;

import java.io.*;

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

@RestController
public class AjaxController {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MongoOperations mongoOperation;

	@RequestMapping(value = "/map/accidents")
	public String getTrafficIncidents() {
		//TAIResponse response = null;
		//try {
		//	OkHttpClient httpClient = new OkHttpClient();
		//	Request request = new Request.Builder().url(new TAIRequest().setBounds(49, 14, 55, 24).addSeverity(Severity.SERIOUS).addSeverity(Severity.MINOR).build()).build();
	    //	response = new TAIResponse(httpClient.newCall(request).execute().body().string());
		//} catch (IOException e) {

		//}
		//if (response != null)
		//	return  response.getWholeResponse().toString();
		//else



        // DBCollection collection = mongoOperation.createCollection("test_collection");
		//String json = JSON.serialize( jsonObject );
		//DBObject bson = ( DBObject ) JSON.parse( json );
		//collection.insert(bson);

		Resource resource =
				applicationContext.getResource("resources/example.json");
		InputStream is = null;
		try {
			 is = resource.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("File not found expetion");
		}

		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(resource.getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}



		JSONObject jsonObject = (JSONObject) obj;

        org.json.JSONObject myObject = new org.json.JSONObject(jsonObject.toString());

        testJsons(myObject);

		return jsonObject.toString();
	}

    private void testJsons(org.json.JSONObject jsonObject) {
        org.json.JSONArray array = jsonObject.getJSONArray("resourceSets");
        org.json.JSONObject jsonObject1 = array.getJSONObject(0);
        org.json.JSONArray rsrcs = jsonObject1.getJSONArray("resources");
        JSONIterator jsonIterator = new JSONIterator(rsrcs);
        while (jsonIterator.hasNext()) {
            org.json.JSONObject incident = jsonIterator.next();
            ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new MicrosoftDateFromat());
			mapper.registerModule(new GeoJsonModule());
			mapper.registerModule(new GeoModule());
			mapper.registerModule(new GeoModuleExt());

			IncidentItem incidentItem = null;
            try {
                incidentItem = mapper.readValue(incident.toString(), IncidentItem.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (incidentItem != null)
                mongoOperation.save(incidentItem);
        }
    }



}
