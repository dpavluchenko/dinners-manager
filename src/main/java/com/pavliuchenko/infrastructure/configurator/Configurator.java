package com.pavliuchenko.infrastructure.configurator;

import com.pavliuchenko.infrastructure.ApplicationContext;

public interface Configurator {
    void configure(Object o, ApplicationContext context);
}
