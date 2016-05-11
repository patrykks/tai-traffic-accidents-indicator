package pl.edu.agh.tai.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import pl.edu.agh.tai.web.bing.map.utils.GeoModuleExt;
import pl.edu.agh.tai.web.bing.map.utils.MicrosoftDateFromat;
import pl.edu.agh.tai.web.bing.map.utils.MongoUpdater;

import java.util.Arrays;

/**
 * Created by root on 4/05/16.
 */
@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Override
    public String getDatabaseName() {
        return "accidents";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {

        MongoClientURI mongoURI = new MongoClientURI("yourMongoUri");

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
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        return mapper;
    }

}
