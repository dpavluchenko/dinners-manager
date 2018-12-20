package controller;

import controller.action.session.SessionAction;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServlet;
@Log
abstract public class SessionController<T extends SessionAction> extends HttpServlet {

    protected Class<T> actionClass;

    protected SessionAction getAction() {
        try {
            return actionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.throwing(this.getClass().getName(), null, e);
            throw new RuntimeException(e);
        }
    }
}
