package controller.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSaveModel {
    private Long orderId;
    private Long mealId;
    private boolean isChosen;
}
