package controller.dto.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuViewModel {
    private String date;
    private List<MealViewModel> meals;
}
