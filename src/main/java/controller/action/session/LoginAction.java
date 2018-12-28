package controller.action.session;

import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.User;
import security.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginAction implements SessionAction{

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);
        User user = userDataMapper.findByUsername(username);
        if (user.isPresent() && user.checkPasswordIdentity(password)){
            HttpSession session = request.getSession(true);
            session.setAttribute(userAttrName, new UserDetails(user));
            response.setStatus(200);
        } else response.setStatus(401);
    }
}
