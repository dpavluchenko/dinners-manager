package controller;

import controller.action.session.RegisterAction;
import controller.action.session.SessionAction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(urlPatterns = "/api/register")
public class RegisterController extends SessionController<RegisterAction>{
    @Override
    public void init() throws ServletException {
        super.init();
        actionClass = RegisterAction.class;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionAction register = getAction();
        register.process(req, resp);
    }
}
