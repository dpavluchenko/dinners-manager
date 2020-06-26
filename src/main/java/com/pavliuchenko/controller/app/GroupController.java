package com.pavliuchenko.controller.app;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.controller.dto.group.GroupCreateModel;
import com.pavliuchenko.controller.dto.group.GroupUpdateModel;
import com.pavliuchenko.dao.client.GroupDataMapper;
import com.pavliuchenko.domain.Group;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/groups/*", name = "groups")
public class GroupController extends BaseController {

    private GroupDataMapper groupDataMapper;

    @Override
    public void init(ServletConfig config) {
        groupDataMapper = getObject(config, GroupDataMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            List<Group> groups = groupDataMapper.findAll();
            HttpDataBinder.writeDataToResponse(groups, response);
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            GroupCreateModel model = HttpDataBinder.getModelFromRequest(request, GroupCreateModel.class);
            Group group = groupDataMapper.create(new Group(model.getName(), model.getDinnerTime(), false));
            HttpDataBinder.writeDataToResponse(group, response);
        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String method = getMethodName(req);
        if (method.equals("change")) update(req, resp);
        else if (method.equals("default")) setDefault(req, resp);
        else resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            Long groupId = Long.valueOf(HttpDataBinder.getParameterFromRequest(request, "groupId"));
            groupDataMapper.delete(groupId);
        });
    }

    private void setDefault(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            Long groupId = Long.valueOf(HttpDataBinder.getParameterFromRequest(request, "groupId"));
            groupDataMapper.setDefaultGroup(groupId);
        });
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            GroupUpdateModel model = HttpDataBinder.getModelFromRequest(request, GroupUpdateModel.class);
            Group group = new Group(model.getName(), model.getDinnerTime(), false);
            group.setId(model.getGroupId());
            groupDataMapper.update(group);
        });
    }
}
