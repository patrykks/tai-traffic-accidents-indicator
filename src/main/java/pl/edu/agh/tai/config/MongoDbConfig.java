package pl.edu.agh.tai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import pl.edu.agh.tai.utils.GeoModuleExt;
import pl.edu.agh.tai.utils.MicrosoftDateFromat;
import pl.edu.agh.tai.utils.MongoDBUpdater;

@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Autowired
    private Environment env;

    @Override
    public String getDatabaseName() {
        return env.getProperty("mongodb.accidentDB");
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        MongoClientURI mongoURI = new MongoClientURI(createMongoURI());
        return new MongoClient(mongoURI);
    }

    @Bean
    public MongoDBUpdater mongoUpdater() {
        return new MongoDBUpdater();
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
        String user = env.getProperty("mongodb.user");
        String password = env.getProperty("mongodb.password");
        String baseUrl = env.getProperty("mongodb.baseUrl");
        String port = env.getProperty("mongodb.port");
        String accidentDB = env.getProperty("mongodb.accidentDB");

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
