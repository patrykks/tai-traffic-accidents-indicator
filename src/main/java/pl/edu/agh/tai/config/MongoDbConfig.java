package pl.edu.agh.tai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import pl.edu.agh.tai.web.bing.map.utils.GeoModuleExt;
import pl.edu.agh.tai.web.bing.map.utils.MicrosoftDateFromat;
import pl.edu.agh.tai.web.bing.map.utils.MongoUpdater;
import pl.edu.agh.tai.web.bing.map.utils.TAIMongoSettings;

import java.io.IOException;

@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Autowired
    private TAIMongoSettings mongoSettings;

    @Override
    public String getDatabaseName() {
        return mongoSettings.getProperty("accidentDB");
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        MongoClientURI mongoURI = new MongoClientURI(createMongoURI());
        return new MongoClient(mongoURI);
    }

    @Bean
    public MongoUpdater mongoUpdater() {
        return new MongoUpdater();
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new MicrosoftDateFromat());
        mapper.registerModule(new GeoJsonModule());
        mapper.registerModule(new GeoModule());
        mapper.registerModule(new GeoModuleExt());
        return mapper;
    }

    public String createMongoURI() {
        String user = mongoSettings.getProperty("user");
        String password = mongoSettings.getProperty("password");
        String baseUrl = mongoSettings.getProperty("baseUrl");
        String port = mongoSettings.getProperty("port");
        String accidentDB = mongoSettings.getProperty("accidentDB");

        String completeUrl = new StringBuilder("mongodb://")
                .append(user).append(":")
                .append(password).append("@")
                .append(baseUrl).append(":")
                .append(port).append("/")
                .append(accidentDB)
                .toString();
        return completeUrl;
    }
}
