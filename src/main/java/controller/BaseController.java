package controller;

import config.ApplicationProperties;

import javax.servlet.http.HttpServlet;

public abstract class BaseController extends HttpServlet {

    private ApplicationProperties properties = ApplicationProperties.getInstance();

    protected String getUserSessionKey() {
        return properties.getProperty("session.user.key");
    }
}
