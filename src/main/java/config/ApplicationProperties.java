package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ApplicationProperties {

    private Properties properties;
    private static final ApplicationProperties props = new ApplicationProperties();

    private ApplicationProperties() {
        properties = new Properties();
        try (InputStream stream = ApplicationProperties.class.
                getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Error loading application properties");
        }
    }

    public static ApplicationProperties getInstance() {
        return props;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }


}
