package dao.client;

import domain.DayMenu;
import domain.MealType;
import domain.Period;
import domain.dto.MenuDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DayMenuDataMapper extends DataMapper<Long, DayMenu>{
    void clear();
    DayMenu create(DayMenu menu);
    void update(Long id, Map<MealType, String> meals);
    LocalDate delete(Long id);
    List<MenuDetails> findByPeriod(Period period);
    LocalDate findMaxMealDate();
}
