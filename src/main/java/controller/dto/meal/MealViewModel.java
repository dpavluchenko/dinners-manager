package controller.dto.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class MealViewModel {
    private Long id;
    private String name;
    private String type;
}
