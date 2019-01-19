package controller.app;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import dao.client.Page;
import dao.client.SimpleDataMapperFactory;
import dao.client.UserDataMapper;
import domain.dto.UserInfo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/users/*", name = "users")
public class UserController extends BaseController {

    private final UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String method = getMethodName(req);
        if (method.equals("list")) findAll(req, resp);
        else if (method.equals("search")) search(req, resp);
        else resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            Long userId = Long.valueOf(HttpDataBinder.getParameterFromRequest(request, "userId"));
            Long groupId = Long.valueOf(HttpDataBinder.getParameterFromRequest(request, "groupId"));
            userDataMapper.update(userId, groupId);
        });
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            Long userId = Long.valueOf(HttpDataBinder.getParameterFromRequest(request, "userId"));
            userDataMapper.delete(userId);
        });
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            int page = Integer.valueOf(HttpDataBinder.getParameterFromRequest(request,"page"));
            int size = Integer.valueOf(HttpDataBinder.getParameterFromRequest(request,"size"));
            Page<UserInfo> users = userDataMapper.findAll(page, size);
            HttpDataBinder.writeDataToResponse(users, response);
        });
    }

    private void search(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            String fullName = HttpDataBinder.getParameterFromRequest(request,"fullName");
            List<UserInfo> users = userDataMapper.searchByFullName(fullName);
            HttpDataBinder.writeDataToResponse(users, response);
        });
    }
}
