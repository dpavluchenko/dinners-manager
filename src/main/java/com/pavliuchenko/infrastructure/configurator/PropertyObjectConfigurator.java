package com.pavliuchenko.infrastructure.configurator;

import com.pavliuchenko.config.ApplicationProperties;
import com.pavliuchenko.infrastructure.ApplicationContext;
import com.pavliuchenko.infrastructure.annotation.Property;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class PropertyObjectConfigurator implements Configurator {

    @Override
    @SneakyThrows
    public void configure(Object o, ApplicationContext context) {
        Class<?> impl = o.getClass();

        for (Field field : impl.getDeclaredFields()) {
            Property annotation = field.getAnnotation(Property.class);
            if (Objects.nonNull(annotation)) {
                ApplicationProperties properties = context.getObject(ApplicationProperties.class);
                field.setAccessible(true);
                String property = properties.getProperty(annotation.value());
                if (Number.class.isAssignableFrom(field.getType())) {
                    field.set(o, NumberUtils.createNumber(property));
                } else {
                    field.set(o, property);
                }
            }
        }
    }
}
