package controller;

import controller.action.app.ApplicationAction;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServlet;
@Log
public abstract class ApplicationController<T extends ApplicationAction> extends HttpServlet {

    protected Class<T> actionClass;

    protected ApplicationAction getAction() {
        try {
            return actionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.throwing(this.getClass().getName(), null, e);
            throw new RuntimeException(e);
        }
    }
}
