package domain;

import controller.dto.order.OrderSaveModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Entity{
    private Long userId;
    private Long mealId;
    private boolean isChosen;


    public static List<Order> convertTo(List<OrderSaveModel> models, Long userId) {
        return models.stream().map(model -> {
            Order order = new Order(userId, model.getMealId(), model.isChosen());
            order.setId(model.getOrderId());
            return order;
        }).collect(Collectors.toList());
    }
}
