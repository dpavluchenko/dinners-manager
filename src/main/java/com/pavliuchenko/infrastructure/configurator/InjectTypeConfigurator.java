package com.pavliuchenko.infrastructure.configurator;

import com.pavliuchenko.infrastructure.ApplicationContext;
import com.pavliuchenko.infrastructure.annotation.InjectType;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class InjectTypeConfigurator implements Configurator {

    @Override
    @SneakyThrows
    public void configure(Object o, ApplicationContext context) {
        Class<?> impl = o.getClass();
        for (Field field : impl.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectType.class)) {
                field.setAccessible(true);
                field.set(o, context.getObject(field.getType()));
            }
        }

    }
}
