package com.pavliuchenko.controller.session;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.controller.dto.UserRegisterModel;
import com.pavliuchenko.dao.client.GroupDataMapper;
import com.pavliuchenko.dao.client.UserDataMapper;
import com.pavliuchenko.domain.User;
import com.pavliuchenko.security.UserDetails;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/api/register", name = "register")
public class RegisterController extends BaseController {

    private UserDataMapper userDataMapper;
    private GroupDataMapper groupDataMapper;

    @Override
    public void init(ServletConfig config) {
       userDataMapper = getObject(config, UserDataMapper.class);
       groupDataMapper = getObject(config, GroupDataMapper.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserRegisterModel model = HttpDataBinder.getModelFromRequest(request, UserRegisterModel.class);
            Long groupId = groupDataMapper.getDefaultGroupId();
            User user = userDataMapper.create(new User(
                    model.getUsername(),
                    model.getPassword(),
                    model.getFullName(),
                    model.getRole(),
                    groupId));
            HttpSession session = request.getSession(true);
            UserDetails userDetails = new UserDetails(user);
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", userDetails.getFullName());
            userInfo.put("role", userDetails.getRole().name());
            session.setAttribute(getUserSessionKey(), userDetails);
            response.setStatus(HttpServletResponse.SC_OK);
            HttpDataBinder.writeDataToResponse(userInfo, response);
        });
    }
}
