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

@WebServlet(urlPatterns = "/api/manage/users", name = "users")
public class UserController extends BaseController {

    private final UserDataMapper userDataMapper = SimpleDataMapperFactory.getDataMapperFor(UserDataMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            int page = Integer.valueOf(request.getParameter("page"));
            int size = Integer.valueOf(request.getParameter("size"));
            Page<UserInfo> users = userDataMapper.findAll(page, size);
            HttpDataBinder.writeDataToResponse(users, response);
        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {

        });
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {

        });
    }
}
