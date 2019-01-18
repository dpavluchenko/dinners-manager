package controller.session;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/api/login", name = "login")
public class LoginController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            String username = HttpDataBinder.getParameterFromRequest(request,"username");
            String password = HttpDataBinder.getParameterFromRequest(request,"password");
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
        User user = userDataMapper.findByUsername(username);
        if (user.isPresent() && user.checkPasswordIdentity(password)){
            HttpSession session = request.getSession(true);
            session.setAttribute(getUserSessionKey(), new UserDetails(user));
            response.setStatus(200);
        } else response.setStatus(401);
        });
    }

}
