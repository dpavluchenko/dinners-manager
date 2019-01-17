package controller.dto.order;

import domain.dto.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class MenuOrderModel {
    private Long menuId;
    private String day;
    private String date;
    private List<OrderDetails> orderDetails;
}
