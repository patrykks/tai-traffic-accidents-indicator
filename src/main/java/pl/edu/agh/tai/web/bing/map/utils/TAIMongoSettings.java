package pl.edu.agh.tai.web.bing.map.utils;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class TAIMongoSettings extends Properties {
    public static final String DEFAULT_SETTINGS_FILE = "mongo.properties";

    public TAIMongoSettings() throws IOException {
        this(DEFAULT_SETTINGS_FILE);
    }

    public TAIMongoSettings(String settingsFile) throws IOException {
        InputStream settingsStream = getClass().getClassLoader().getResourceAsStream(settingsFile);

        if (settingsStream == null) {
            throw new FileNotFoundException("Mongo settings file " + settingsFile + " doesn't exist in classpath");
        }

        load(settingsStream);
    }

}
