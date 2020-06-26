package com.pavliuchenko.infrastructure;

import com.pavliuchenko.infrastructure.annotation.Singleton;
import com.pavliuchenko.infrastructure.config.Config;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    @Getter
    private Config<?> config;
    @Setter
    private ObjectFactory objectFactory;
    private Map<Class, Object> cache;

    public ApplicationContext(Config<?> config) {
        this.config = config;
        this.cache = new ConcurrentHashMap<>();
    }

    public <T> T getObject(Class<T> type) {
        if (cache.containsKey(type)) return (T) cache.get(type);

        Class<? extends T> impl = type.isInterface() ? config.getImplClassFor(type) : type;

        T object = objectFactory.createObject(impl);

        if (impl.isAnnotationPresent(Singleton.class)) cache.put(type, object);

        return object;
    }


}
