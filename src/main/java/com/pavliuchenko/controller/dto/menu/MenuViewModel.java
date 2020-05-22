package com.pavliuchenko.controller.dto.menu;

import com.pavliuchenko.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MenuViewModel {
    private Long menuId;
    private String day;
    private String date;
    private Map<MealType, String> meals;
    public static final int CURRENT_MENU_DAYS = 10;
}
