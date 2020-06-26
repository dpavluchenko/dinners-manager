package com.pavliuchenko.infrastructure.config;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config<Class<? extends Annotation>> {

    private Map<Class, Class> implsByInterface;

    private Reflections packageScanner;

    public JavaConfig(Map<Class, Class> implsByInterface, String configPath) {
        this.implsByInterface = implsByInterface;
        this.packageScanner = new Reflections(configPath);
    }

    @Override
    public <T> Class<? extends T> getImplClassFor(Class<T> ifc) {
        return implsByInterface.computeIfAbsent(ifc, aClass -> findImplClass(ifc));
    }

    @Override
    public <T> List<Class<? extends T>> getImplClassesFor(Class<T> ifc) {
        return new ArrayList<>(packageScanner.getSubTypesOf(ifc));
    }

    @Override
    public List<Class> getClassesBy(Class<? extends Annotation> type) {
        return new ArrayList<>(packageScanner.getTypesAnnotatedWith(type));
    }

    private <T> Class<? extends T> findImplClass(Class<T> ifc) {
        Set<Class<? extends T>> impls = packageScanner.getSubTypesOf(ifc);

        if (impls.size() != 1) {
            throw new IllegalStateException(String.format("Class %s has zero or more than one implementations!", ifc));
        }

        return impls.iterator().next();
    }

}
