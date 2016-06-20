package pl.edu.agh.tai.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import pl.edu.agh.tai.persistence.MongoDBUpdater;

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
