package com.pavliuchenko.controller.app;

import com.pavliuchenko.controller.BaseController;
import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.controller.dto.menu.MenuCreateModel;
import com.pavliuchenko.controller.dto.menu.MenuUpdateModel;
import com.pavliuchenko.controller.dto.menu.MenuViewModel;
import com.pavliuchenko.dao.client.DayMenuDataMapper;
import com.pavliuchenko.domain.DayMenu;
import com.pavliuchenko.domain.DinnerCalendar;
import com.pavliuchenko.domain.Period;
import com.pavliuchenko.domain.event.AddMenuEvent;
import com.pavliuchenko.domain.event.EventManager;
import com.pavliuchenko.domain.event.RemoveMenuEvent;
import com.pavliuchenko.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/menu", name = "menu")
public class DayMenuController extends BaseController {

    private EventManager eventManager;

    private DayMenuDataMapper dayMenuDataMapper;

    private DinnerCalendar dinnerCalendar;

    @Override
    public void init(ServletConfig config) {
        eventManager = getObject(config, EventManager.class);
        dayMenuDataMapper = getObject(config, DayMenuDataMapper.class);
        dinnerCalendar = getObject(config, DinnerCalendar.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp, (request, response) -> {
            String date = request.getParameter("date");
            List<MenuViewModel> modelsToResp;
            if (date != null) {
                modelsToResp = DayMenu.convert(dayMenuDataMapper.findByDate(DateUtil.parseLocalDate(date)));
            } else {
                modelsToResp = DayMenu.convert(dayMenuDataMapper.findByPeriod(findMenuPeriod()));
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

    private Period findMenuPeriod() {
        Period period = dinnerCalendar.findMenuPeriod();
        int diff = period.calcBetweenPeriod();
        if (diff < MenuViewModel.CURRENT_MENU_DAYS) {
            period.minusStartDate(MenuViewModel.CURRENT_MENU_DAYS - diff);
        }
        return period;
    }
}
