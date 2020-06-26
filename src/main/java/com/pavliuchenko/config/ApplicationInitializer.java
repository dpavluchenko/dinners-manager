package com.pavliuchenko.config;

import com.pavliuchenko.infrastructure.Application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

import static com.pavliuchenko.config.AppConstants.APP_CONTEXT;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(APP_CONTEXT, Application.run(new HashMap<>(), "com.pavliuchenko"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
