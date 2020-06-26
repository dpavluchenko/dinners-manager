package com.pavliuchenko.controller.app;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.dao.client.OrderDataMapper;
import com.pavliuchenko.domain.Order;
import com.pavliuchenko.domain.dto.OrderStatistic;
import com.pavliuchenko.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/api/manage/stats", name = "statistic")
public class StatisticController extends BaseController {

    private OrderDataMapper orderDataMapper;

    @Override
    public void init(ServletConfig config) {
        orderDataMapper = getObject(config, OrderDataMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            LocalDate date = DateUtil.parseLocalDate(HttpDataBinder.getParameterFromRequest(request, "date"));
            List<OrderStatistic> statisticByDate = orderDataMapper.findStatisticByDate(date);
            Map<String, Object> model = new HashMap<>();
            model.put("statistic", statisticByDate);
            model.put("total", Order.calculateTotalOrders(statisticByDate));
            HttpDataBinder.writeDataToResponse(model, response);
        });
    }
}
