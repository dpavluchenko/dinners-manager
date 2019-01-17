package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuOrder {
    private Long menuId;
    private LocalDate date;
    private List<OrderDetails> orderDetails;
}
