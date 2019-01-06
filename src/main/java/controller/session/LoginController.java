package controller.session;

import controller.BaseController;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/api/login")
public class LoginController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
        User user = userDataMapper.findByUsername(username);
        if (user.isPresent() && user.checkPasswordIdentity(password)){
            HttpSession session = req.getSession(true);
            session.setAttribute(getUserSessionKey(), new UserDetails(user));
            resp.setStatus(200);
        } else resp.setStatus(401);
    }

}
