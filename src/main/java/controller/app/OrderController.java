package controller.app;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import controller.dto.order.MenuOrderModel;
import controller.dto.order.OrderSaveModel;
import dao.client.OrderDataMapper;
import dao.client.SimpleDataMapperFactory;
import domain.DinnerCalendar;
import domain.Order;
import domain.dto.GroupOrderDetails;
import domain.dto.MenuOrder;
import security.UserDetails;
import util.DateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/api/orders/*", name = "orders")
public class OrderController extends BaseController {

    private final OrderDataMapper orderDataMapper = SimpleDataMapperFactory.getDataMapperFor(OrderDataMapper.class);

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
            orderDataMapper.save(Order.convertTo(models, userDetails.getUserId()));
        });
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            UserDetails userDetails = (UserDetails) request.getSession(false).getAttribute(getUserSessionKey());
            List<MenuOrder> menuOrders = orderDataMapper.findForUserByPeriod(DinnerCalendar.getInstance().findMenuPeriod(), userDetails.getUserId());
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
