package com.pavliuchenko.controller.session;

import com.pavliuchenko.controller.BaseController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/api/logout", name = "logout")
public class LogoutController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
        });
    }
}
