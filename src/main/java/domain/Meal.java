package domain;

import controller.dto.meal.MealViewModel;
import controller.dto.meal.MenuCreateModel;
import controller.dto.meal.MenuViewModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class Meal extends Entity{
    private String name;
    private LocalDate date;
    private MealType type;

    public Meal(String name, String date, MealType type) {
        this.name = name;
        this.date = DateUtil.parseLocalDate(date);
        this.type = type;
    }

    public static List<Meal> createFromModel(MenuCreateModel model) {
        return model.getMenu().entrySet().stream().map(meal -> new Meal(meal.getValue(),
                model.getDate(), meal.getKey())).collect(Collectors.toList());
    }

    public static MenuViewModel convertTo(List<Meal> meals){
        List<MealViewModel> models = meals.stream().map(meal -> new MealViewModel(meal.id, meal.name, meal.type.name())).
                collect(Collectors.toList());
        return new MenuViewModel(DateUtil.dateToString(meals.get(0).getDate()), models);
    }
}
