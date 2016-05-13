package pl.edu.agh.tai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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

    @Override
    public String getDatabaseName() {
        return "accidents";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {

        MongoClientURI mongoURI = new MongoClientURI("mongodb://patrykks:uzumymw@ds031223.mlab.com:31223/accidents");

        return new MongoClient(mongoURI);
    }

    @Bean
    public MongoUpdater mongoUpdater() {
        return new MongoUpdater();
    }

    @Bean
    public TAIMongoSettings mongoSettings() throws IOException {
        TAIMongoSettings mongoSettings = new TAIMongoSettings();
        return mongoSettings;
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

}
