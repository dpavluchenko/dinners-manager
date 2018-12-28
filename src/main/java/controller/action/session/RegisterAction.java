package controller.action.session;

import controller.binder.HttpDataBinder;
import controller.dto.UserRegisterModel;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterAction implements SessionAction{
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        UserRegisterModel model = HttpDataBinder.getDataFromRequest(request, UserRegisterModel.class);
        User user = new User(model.getUsername(), model.getPassword(), model.getFullName(), model.getRole());
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
        Long userId = userDataMapper.create(user);
        user.setId(userId);
        UserDetails userDetails = new UserDetails(user);
        HttpSession session = request.getSession(true);
        session.setAttribute(userAttrName, userDetails);
        response.setStatus(200);
    }
}
