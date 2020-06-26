package com.pavliuchenko.controller.dto.menu;

import com.pavliuchenko.domain.MealType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MenuCreateModel {
    @NonNull
    private String date;
    @NonNull
    private Map<MealType, String> meals;
}
