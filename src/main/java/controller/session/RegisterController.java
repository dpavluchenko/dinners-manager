package controller.session;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import controller.dto.UserRegisterModel;
import dao.client.GroupDataMapper;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet(urlPatterns = "/api/register", name = "register")
public class RegisterController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserRegisterModel model = HttpDataBinder.getModelFromRequest(request, UserRegisterModel.class);
            UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
            Long groupId = SimpleDataMapperFactory.getDataMapperFor(GroupDataMapper.class).getDefaultGroupId();
            User user = userDataMapper.create(new User(
                    model.getUsername(),
                    model.getPassword(),
                    model.getFullName(),
                    model.getRole(),
                    groupId));
            HttpSession session = request.getSession(true);
            session.setAttribute(getUserSessionKey(), new UserDetails(user));
            response.setStatus(HttpServletResponse.SC_OK);
        });
    }
}
