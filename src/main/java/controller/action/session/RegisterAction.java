package controller.action.session;

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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");
        User user = new User(username, password, fullName, role);
        UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(User.class);
        Long userId = userDataMapper.create(user);
        user.setId(userId);
        UserDetails userDetails = new UserDetails(user);
        HttpSession session = request.getSession(true);
        session.setAttribute(userAttrName, userDetails);
        response.setStatus(200);
    }
}
