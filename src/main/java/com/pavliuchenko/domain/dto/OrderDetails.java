package com.pavliuchenko.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetails {
    private Long orderId;
    private Long mealId;
    private String name;
    private String type;
    private boolean chosen;
}
