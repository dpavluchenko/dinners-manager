package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class OrderStatistic {
    private String groupName;
    private String dinnerTime;
    private Map<String, Integer> countByType;
}
