package controller;

import controller.action.session.LogoutAction;
import controller.action.session.SessionAction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(urlPatterns = "/api/logout")
public class LogoutController extends SessionController<LogoutAction>{
    @Override
    public void init() throws ServletException {
        super.init();
        actionClass = LogoutAction.class;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionAction logout = getAction();
        logout.process(req, resp);
    }
}
