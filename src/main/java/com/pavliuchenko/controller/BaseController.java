package com.pavliuchenko.controller;

import com.pavliuchenko.config.ApplicationProperties;
import com.pavliuchenko.controller.handler.ExceptionHandler;
import com.pavliuchenko.controller.handler.RequestProcessor;
import com.pavliuchenko.infrastructure.ApplicationContext;
import lombok.AccessLevel;
import lombok.Getter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.pavliuchenko.config.AppConstants.APP_CONTEXT;

public abstract class BaseController extends HttpServlet {

    @Getter(value = AccessLevel.PROTECTED)
    private String userSessionKey;

    @Override
    public void init(ServletConfig config) {
        userSessionKey = getObject(config, ApplicationProperties.class).getProperty("session.user.key");
    }

    protected <T> T getObject(ServletConfig config, Class<T> type){
        ApplicationContext context = (ApplicationContext) config.getServletContext().getAttribute(APP_CONTEXT);
        return context.getObject(type);
    }

    protected String getMethodName(HttpServletRequest request) {
        return request.getPathInfo().replaceAll("/", "");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, RequestProcessor processor) {
        try {
            processor.process(request, response);
        } catch (Exception e) {
            ExceptionHandler.handle(response, e);
        }
    }
}
