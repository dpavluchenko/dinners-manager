package controller;

import config.ApplicationProperties;
import controller.handler.ExceptionHandler;
import controller.handler.RequestProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController extends HttpServlet {

    private ApplicationProperties properties = ApplicationProperties.getInstance();

    protected String getUserSessionKey() {
        return properties.getProperty("session.user.key");
    }

    protected String getMethodName(HttpServletRequest request) {
        String url = request.getRequestURI();
        return url.substring(url.lastIndexOf("/") + 1);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, RequestProcessor processor) {
        try {
            processor.process(request, response);
        } catch (Exception e) {
            ExceptionHandler.handle(response, e);
        }
    }
}
