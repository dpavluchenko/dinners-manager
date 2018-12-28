package dao.client;

import domain.Meal;

import java.time.LocalDate;

public interface MealDataMapper extends DataMapper<Long, Meal>{
    LocalDate findMaxMealDate();
}
