package dao.client;

import domain.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealDataMapper extends DataMapper<Long, Meal>{
    LocalDate findMaxMealDate();

    List<Meal> create(List<Meal> meals);
}
