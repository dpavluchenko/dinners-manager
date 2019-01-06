package dao.impl;

import dao.client.OrderDataMapper;
import domain.Order;

import java.sql.Date;
import java.sql.ResultSet;

public class OrderDataMapperPgImpl extends AbstractDataMapper implements OrderDataMapper {

    private static OrderDataMapper dataMapper = new OrderDataMapperPgImpl();

    private OrderDataMapperPgImpl(){}

    @Override
    public Order create(Order order) {
        String sql = "insert into orders (date, user_id, meal_id) values (?, ?, ?) returning id";
        Long id = executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(order.getDate()));
            ps.setLong(2, order.getUserId());
            ps.setLong(3, order.getMealId());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
        order.setId(id);
        return order;
    }

    @Override
    public Order findById(Long id) {
        String sql = "select * from orders where id=?";
        return executeQuery(sql, ps-> {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Order order = new Order();
            if (rs.next()) {
                order.setId(rs.getLong("id"));
                order.setDate(rs.getDate("date").toLocalDate());
                order.setUserId(rs.getLong("user_id"));
                order.setMealId(rs.getLong("meal_id"));
            }
            return order;
        });
    }

    public static OrderDataMapper getInstance(){
        return dataMapper;
    }
}
