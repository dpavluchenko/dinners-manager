package controller.app;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import dao.client.OrderDataMapper;
import dao.client.SimpleDataMapperFactory;
import domain.dto.OrderStatistic;
import util.DateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/stats", name = "statistic")
public class StatisticController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            OrderDataMapper orderDataMapper = SimpleDataMapperFactory.getDataMapperFor(OrderDataMapper.class);
            LocalDate date = DateUtil.parseLocalDate(HttpDataBinder.getParameterFromRequest(request, "date"));
            List<OrderStatistic> statisticByDate = orderDataMapper.findStatisticByDate(date);
            HttpDataBinder.writeDataToResponse(statisticByDate, response);
        });
    }
}
