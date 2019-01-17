package domain;

import controller.dto.menu.MenuCreateModel;
import controller.dto.menu.MenuViewModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Meal extends Entity{
    private Long menuId;
    private String name;
    private MealType type;

    public Meal(String name, MealType type) {
        this.name = name;
        this.type = type;
    }

}
