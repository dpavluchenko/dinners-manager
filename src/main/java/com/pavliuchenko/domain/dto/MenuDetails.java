package com.pavliuchenko.domain.dto;

import com.pavliuchenko.domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class MenuDetails {
    private Long menuId;
    private LocalDate date;
    private Map<MealType, String> meals;
}
