package controller.dto.meal;

import domain.MealType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MenuCreateModel {
    private String date;
    private Map<MealType, String> menu;
}
