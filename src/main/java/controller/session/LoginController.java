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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/api/login", name = "login")
public class LoginController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            HttpSession session = request.getSession(false);
            if (session != null) {
                UserDetails userDetails = (UserDetails) session.getAttribute(getUserSessionKey());
                addUserInfo(response, userDetails);
            } else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            String username = HttpDataBinder.getParameterFromRequest(request,"username");
            String password = HttpDataBinder.getParameterFromRequest(request,"password");
            UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
            User user = userDataMapper.findByUsername(username);
            if (user.isPresent() && user.checkPasswordIdentity(password)) {
                HttpSession session = request.getSession(true);
                UserDetails userDetails = new UserDetails(user);
                session.setAttribute(getUserSessionKey(), userDetails);
                addUserInfo(response, userDetails);
                response.setStatus(HttpServletResponse.SC_OK);
            } else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }

    private void addUserInfo(HttpServletResponse response, UserDetails userDetails) {
        Map<String, String> model = new HashMap<>();
        model.put("name", userDetails.getFullName());
        model.put("role", userDetails.getRole().name());
        HttpDataBinder.writeDataToResponse(model, response);
    }

}
