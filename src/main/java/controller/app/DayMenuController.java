package controller.app;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import controller.dto.menu.MenuCreateModel;
import controller.dto.menu.MenuUpdateModel;
import controller.dto.menu.MenuViewModel;
import dao.client.DayMenuDataMapper;
import dao.client.SimpleDataMapperFactory;
import domain.DayMenu;
import domain.DinnerCalendar;
import domain.event.AddMenuEvent;
import domain.event.EventManager;
import domain.event.RemoveMenuEvent;
import util.DateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/menu", name = "menu")
public class DayMenuController extends BaseController {

    private final EventManager eventManager = EventManager.getInstance();

    private final DayMenuDataMapper dayMenuDataMapper = SimpleDataMapperFactory.getDataMapperFor(DayMenuDataMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            String date = request.getParameter("date");
            List<MenuViewModel> modelsToResp;
            if (date != null) {
                modelsToResp = DayMenu.convert(dayMenuDataMapper.findByDate(DateUtil.parseLocalDate(date)));
            } else {
                modelsToResp = DayMenu.convert(dayMenuDataMapper.findByPeriod(DinnerCalendar.getInstance().findMenuPeriod()));
            }
            HttpDataBinder.writeDataToResponse(modelsToResp, response);
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            MenuCreateModel modelFromReq = HttpDataBinder.getModelFromRequest(request, MenuCreateModel.class);
            DayMenu menu = dayMenuDataMapper.create(DayMenu.create(modelFromReq));
            MenuViewModel modelToResp = new MenuViewModel(menu.getId(), DateUtil.getDayOfWeek(menu.getDate()),
                    modelFromReq.getDate(), modelFromReq.getMeals());
            eventManager.publish(new AddMenuEvent(menu.getDate()));
            HttpDataBinder.writeDataToResponse(modelToResp, response);
        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            MenuUpdateModel model = HttpDataBinder.getModelFromRequest(request, MenuUpdateModel.class);
            dayMenuDataMapper.update(model.getMenuId(), model.getMeals());
        });
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            LocalDate date = dayMenuDataMapper.delete(Long.valueOf(request.getParameter("id")));
            eventManager.publish(new RemoveMenuEvent(date));
        });
    }
}
