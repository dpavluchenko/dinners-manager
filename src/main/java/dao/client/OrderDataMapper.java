package dao.client;

import domain.Order;
import domain.Period;
import domain.dto.GroupOrderDetails;
import domain.dto.MenuOrder;
import domain.dto.OrderStatistic;

import java.time.LocalDate;
import java.util.List;

public interface OrderDataMapper extends DataMapper<Long, Order>{

    List<MenuOrder> findForUserByPeriod(Period period, Long userId);

    List<OrderStatistic> findStatisticByDate(LocalDate date);

    List<GroupOrderDetails> findAllInGroup(Long groupId, LocalDate date);

    List<Order> save(List<Order> orders);

}
