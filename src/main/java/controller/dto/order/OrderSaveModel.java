package controller.dto.order;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class OrderSaveModel {
    @NonNull
    private Long orderId;
    @NonNull
    private Long mealId;
    private boolean chosen;
}
