package pl.edu.agh.tai.utils;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TAIMongoDBProperties extends Properties {
    public static final String DEFAULT_SETTINGS_FILE = "mongo.properties";

    public TAIMongoDBProperties() throws IOException {
        this(DEFAULT_SETTINGS_FILE);
    }

    public TAIMongoDBProperties(String settingsFile) throws IOException {
        InputStream settingsStream = getClass().getClassLoader().getResourceAsStream(settingsFile);

        if (settingsStream == null) {
            throw new FileNotFoundException("Mongo settings file " + settingsFile + " doesn't exist in classpath");
        }

        load(settingsStream);
    }

}
