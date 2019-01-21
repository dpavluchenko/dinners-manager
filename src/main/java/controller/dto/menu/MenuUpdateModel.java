package controller.dto.menu;

import domain.MealType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MenuUpdateModel {
    @NonNull
    private Long menuId;
    @NonNull
    private Map<MealType, String> meals;
}
