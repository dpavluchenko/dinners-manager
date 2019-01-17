package domain;

import controller.dto.menu.MenuCreateModel;
import controller.dto.menu.MenuViewModel;
import domain.dto.MenuDetails;
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
public class DayMenu extends Entity {
    private LocalDate date;
    private List<Meal> meals;


    public DayMenu(String date, List<Meal> meals) {
        this.date = DateUtil.parseLocalDate(date);
        this.meals = meals;
    }

    public static DayMenu create(MenuCreateModel model) {
        List<Meal> meals = model.getMeals().entrySet().stream().map(m ->
                new Meal(m.getValue(), m.getKey())).collect(Collectors.toList());
        return new DayMenu(model.getDate(), meals);
    }

    public static List<MenuViewModel> convert(List<MenuDetails> menuDetails) {
        return menuDetails.stream().map(m -> new MenuViewModel(m.getMenuId(), DateUtil.getDayOfWeek(m.getDate()),
                 DateUtil.dateToString(m.getDate()), m.getMeals())).collect(Collectors.toList());
    }

    public static Period findMenuPeriod(String from, String to) {
        if (from != null && !from.isEmpty()) {
            return new Period(DateUtil.parseLocalDate(from), DateUtil.parseLocalDate(to));
        }
        return DinnerCalendar.getInstance().findMenuPeriod();
    }
}
