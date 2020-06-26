package com.pavliuchenko.controller.app;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.controller.dto.order.MenuOrderModel;
import com.pavliuchenko.controller.dto.order.OrderSaveModel;
import com.pavliuchenko.dao.client.OrderDataMapper;
import com.pavliuchenko.domain.DinnerCalendar;
import com.pavliuchenko.domain.Order;
import com.pavliuchenko.domain.dto.GroupOrderDetails;
import com.pavliuchenko.domain.dto.MenuOrder;
import com.pavliuchenko.security.UserDetails;
import com.pavliuchenko.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/api/orders/*", name = "orders")
public class OrderController extends BaseController {

    private OrderDataMapper orderDataMapper;
    private DinnerCalendar dinnerCalendar;

    @Override
    public void init(ServletConfig config) {
        orderDataMapper = getObject(config, OrderDataMapper.class);
        dinnerCalendar = getObject(config, DinnerCalendar.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String method = getMethodName(req);
        if (method.equals("list")) findAll(req, resp);
        else if (method.equals("details")) findDetailsByGroup(req, resp);
        else resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserDetails userDetails = (UserDetails) request.getSession(false).getAttribute(getUserSessionKey());
            List<OrderSaveModel> models = HttpDataBinder.getListOfModelsFromRequest(request, OrderSaveModel.class);
            List<Order> newOrders = orderDataMapper.save(Order.convertTo(models, userDetails.getUserId()));
            HttpDataBinder.writeDataToResponse(Order.convertTo(newOrders), response);
        });
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserDetails userDetails = (UserDetails) request.getSession(false).getAttribute(getUserSessionKey());
            List<MenuOrder> menuOrders = orderDataMapper.findForUserByPeriod(dinnerCalendar.findMenuPeriod(), userDetails.getUserId());
            List<MenuOrderModel> models = menuOrders.stream().map(menuOrder -> new MenuOrderModel(menuOrder.getMenuId(),
                    DateUtil.getDayOfWeek(menuOrder.getDate()),
                    DateUtil.dateToString(menuOrder.getDate()),
                    menuOrder.getOrderDetails())).collect(Collectors.toList());
            HttpDataBinder.writeDataToResponse(models, response);
        });
    }

    private void findDetailsByGroup(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserDetails userDetails = (UserDetails) request.getSession(false).getAttribute(getUserSessionKey());
            LocalDate date = DateUtil.parseLocalDate(HttpDataBinder.getParameterFromRequest(request, "date"));
            List<GroupOrderDetails> orderDetails = orderDataMapper.findAllInGroup(userDetails.getGroupId(), date);
            HttpDataBinder.writeDataToResponse(orderDetails, response);
        });
    }
}
