package config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
            Class.forName("domain.DinnerCalendar");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't init application classes", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
