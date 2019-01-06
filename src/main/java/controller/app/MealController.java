package controller.app;

import controller.BaseController;
import controller.binder.HttpDataBinder;
import controller.dto.meal.MenuCreateModel;
import dao.client.MealDataMapper;
import dao.client.SimpleDataMapperFactory;
import domain.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/api/manage/meals")
public class MealController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MealDataMapper mealDataMapper = SimpleDataMapperFactory.getDataMapperFor(MealDataMapper.class);
        MenuCreateModel model = HttpDataBinder.getModelFromRequest(req, MenuCreateModel.class);
        List<Meal> meals = mealDataMapper.create(Meal.createFromModel(model));
        HttpDataBinder.writeDataToResponse(Meal.convertTo(meals), resp);
    }
}
