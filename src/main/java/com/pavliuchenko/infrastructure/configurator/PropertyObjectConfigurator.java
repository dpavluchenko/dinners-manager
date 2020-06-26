package com.pavliuchenko.infrastructure.configurator;

import com.pavliuchenko.config.ApplicationProperties;
import com.pavliuchenko.infrastructure.ApplicationContext;
import com.pavliuchenko.infrastructure.annotation.Property;
import lombok.SneakyThrows;

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
                    field.set(o, createNumber(field.getType(), property));
                } else {
                    field.set(o, property);
                }
            }
        }
    }

    private <T extends Number> T createNumber(Class<?> type, String property){
       if (type.equals(Long.class)) return (T) Long.valueOf(property);
       else return (T) Integer.valueOf(property);
    }
}
