package com.pavliuchenko.dao.client;

import com.pavliuchenko.domain.DayMenu;
import com.pavliuchenko.domain.MealType;
import com.pavliuchenko.domain.Period;
import com.pavliuchenko.domain.dto.MenuDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DayMenuDataMapper extends DataMapper<Long, DayMenu>{
    DayMenu create(DayMenu menu);
    void update(Long id, Map<MealType, String> meals);
    LocalDate delete(Long id);
    List<MenuDetails> findByPeriod(Period period);
    List<MenuDetails> findByDate(LocalDate date);
    LocalDate findMaxMealDate();
}
