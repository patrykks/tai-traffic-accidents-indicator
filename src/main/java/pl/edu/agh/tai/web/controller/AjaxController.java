package pl.edu.agh.tai.web.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import pl.edu.agh.tai.web.bing.map.core.TAIRequest;
import pl.edu.agh.tai.web.bing.map.core.TAIResponse;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class AjaxController {

	@Autowired
	private ApplicationContext applicationContext;

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

		return jsonObject.toString();
	}

}
