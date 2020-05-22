package com.pavliuchenko.domain;

import com.pavliuchenko.controller.dto.order.OrderSaveModel;
import com.pavliuchenko.domain.dto.OrderStatistic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
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

    public static List<OrderSaveModel> convertTo(List<Order> orders) {
        return orders.stream().map(o -> {
            OrderSaveModel model = new OrderSaveModel();
            model.setOrderId(o.id);
            model.setMealId(o.mealId);
            model.setChosen(o.isChosen());
            return model;
        }).collect(Collectors.toList());
    }

    public static Map<String, Integer> calculateTotalOrders(List<OrderStatistic> statistics) {
        return statistics.stream().flatMap(os -> os.getCountByType().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (count1, count2) -> count1 + count2));
    }
}
