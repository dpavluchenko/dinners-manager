package com.pavliuchenko.controller.app;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.dao.client.Page;
import com.pavliuchenko.dao.client.UserDataMapper;
import com.pavliuchenko.domain.dto.UserInfo;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/manage/users/*", name = "users")
public class UserController extends BaseController {

    private UserDataMapper userDataMapper;

    @Override
    public void init(ServletConfig config) {
        userDataMapper = getObject(config, UserDataMapper.class);
    }

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
            int page = Integer.valueOf(HttpDataBinder.getParameterFromRequest(request, "page"));
            int size = Integer.valueOf(HttpDataBinder.getParameterFromRequest(request, "size"));
            Page<UserInfo> users = userDataMapper.searchByFullName(fullName, page, size);
            HttpDataBinder.writeDataToResponse(users, response);
        });
    }
}
