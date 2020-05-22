package com.pavliuchenko.infrastructure;

import com.pavliuchenko.infrastructure.configurator.Configurator;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    private List<Configurator> configurators;

    private ApplicationContext applicationContext;

    @SneakyThrows
    public ObjectFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.configurators = new ArrayList<>();
        addConfigurators(applicationContext);
    }

    @SneakyThrows
    public <T> T createObject(Class<T> implementation) {
        T instance = createObjectInstance(implementation);

        configure(instance);

        invokeInitMethod(instance);

        return instance;
    }

    private void configure(Object o) {
        configurators.forEach(configurator -> configurator.configure(o, applicationContext));
    }

    private void invokeInitMethod(Object o) throws InvocationTargetException, IllegalAccessException {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)){
                method.invoke(o);
            }
        }
    }

    private <T> T createObjectInstance(Class<T> implementation) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return implementation.getDeclaredConstructor().newInstance();
    }

    private void addConfigurators(ApplicationContext applicationContext) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<? extends Configurator> aClass : applicationContext.getConfig().getImplClassesFor(Configurator.class)) {
            configurators.add(aClass.getDeclaredConstructor().newInstance());
        }
    }
}
