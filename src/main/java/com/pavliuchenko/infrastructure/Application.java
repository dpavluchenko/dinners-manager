package com.pavliuchenko.infrastructure;

import com.pavliuchenko.infrastructure.annotation.Singleton;
import com.pavliuchenko.infrastructure.config.JavaConfig;

import java.util.List;
import java.util.Map;

public class Application {

    public static ApplicationContext run(Map<Class, Class> implsByInterface, String configPath) {
        JavaConfig config = new JavaConfig(implsByInterface, configPath);
        ApplicationContext context = new ApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(context);
        context.setObjectFactory(objectFactory);
        List<Class> singletons = config.getClassesBy(Singleton.class);
        singletons.forEach(context::getObject);
        return context;
    }
}
