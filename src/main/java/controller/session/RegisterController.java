package controller.session;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import controller.dto.UserRegisterModel;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet(urlPatterns = "/api/register")
public class RegisterController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserRegisterModel model = HttpDataBinder.getModelFromRequest(req, UserRegisterModel.class);
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
        User user = userDataMapper.create(new User(
                model.getUsername(),
                model.getPassword(),
                model.getFullName(),
                model.getRole()));
        UserDetails userDetails = new UserDetails(user);
        HttpSession session = req.getSession(true);
        session.setAttribute(getUserSessionKey(), userDetails);
        resp.setStatus(200);
    }
}
