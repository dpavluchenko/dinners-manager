package com.pavliuchenko.dao.client;

import com.pavliuchenko.domain.Order;
import com.pavliuchenko.domain.Period;
import com.pavliuchenko.domain.dto.GroupOrderDetails;
import com.pavliuchenko.domain.dto.MenuOrder;
import com.pavliuchenko.domain.dto.OrderStatistic;

import java.time.LocalDate;
import java.util.List;

public interface OrderDataMapper extends DataMapper<Long, Order>{

    List<MenuOrder> findForUserByPeriod(Period period, Long userId);

    List<OrderStatistic> findStatisticByDate(LocalDate date);

    List<GroupOrderDetails> findAllInGroup(Long groupId, LocalDate date);

    List<Order> save(List<Order> orders);

}
