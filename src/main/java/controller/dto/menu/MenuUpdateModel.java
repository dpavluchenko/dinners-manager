package controller.dto.menu;

import domain.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MenuUpdateModel {
    private Long menuId;
    private Map<MealType, String> meals;
}
