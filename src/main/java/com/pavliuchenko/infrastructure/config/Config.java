package com.pavliuchenko.infrastructure.config;


import java.util.List;

public interface Config<E> {
    <T> Class<? extends T> getImplClassFor(Class<T> ifc);

    <T> List<Class<? extends T>> getImplClassesFor(Class<T> ifc);

    List<Class> getClassesBy(E type);
}
