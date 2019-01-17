package dao.client;

import domain.Order;
import domain.Period;
import domain.dto.MenuOrder;

import java.util.List;

public interface OrderDataMapper extends DataMapper<Long, Order>{
    List<MenuOrder> findForUserByPeriod(Period period, Long userId);
    void save(List<Order> orders);
    void clear();
}
