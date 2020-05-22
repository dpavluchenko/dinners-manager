package com.pavliuchenko.domain.dto;

import com.pavliuchenko.domain.MealType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class OrderStatistic {
    private String groupName;
    private String dinnerTime;
    private Map<String, Integer> countByType;

    public OrderStatistic(String groupName, String dinnerTime) {
        this.groupName = groupName;
        this.dinnerTime = dinnerTime;
        init();
    }

    private void init() {
        countByType = new HashMap<>();
        for (MealType type : MealType.values()) {
            countByType.put(type.name(), 0);
        }
    }
}
