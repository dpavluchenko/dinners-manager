package com.pavliuchenko.config;

import com.pavliuchenko.infrastructure.annotation.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class ApplicationProperties {

    private Properties properties;

    public ApplicationProperties() {
        properties = new Properties();
        try (InputStream stream = ApplicationProperties.class.
                getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Error loading application properties");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }


}
