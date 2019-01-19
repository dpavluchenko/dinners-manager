package dao.impl;

import dao.client.OrderDataMapper;
import domain.Order;
import domain.Period;
import domain.dto.GroupOrderDetails;
import domain.dto.MenuOrder;
import domain.dto.OrderDetails;
import domain.dto.OrderStatistic;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDataMapperPgImpl extends AbstractDataMapper implements OrderDataMapper {

    private static OrderDataMapper dataMapper = new OrderDataMapperPgImpl();

    private final Long notExistedOrderId = -1L;

    private OrderDataMapperPgImpl(){}

    @Override
    public void save(List<Order> orders) {
        List<Order> newOrders = orders.stream().filter(order -> order.getId().equals(notExistedOrderId)).collect(Collectors.toList());
        List<Order> oldOrders = orders.stream().filter(order -> !order.getId().equals(notExistedOrderId)).collect(Collectors.toList());
        executeTransaction(connection -> {
           if (!newOrders.isEmpty()) insert(connection, newOrders);
           if (!oldOrders.isEmpty()) update(connection, oldOrders);
           return null;
        });
    }

    @Override
    public List<MenuOrder> findForUserByPeriod(Period period, Long userId) {
        String sql = "select menu.id as menu_id, date, m.id as meal_id, m.name, m.type, o.is_chosen, o.id as order_id from day_menu as menu" +
                "  join meals as m on menu.id = m.menu_id and date>=? and date<=?" +
                "  left outer join orders o on m.id = o.meal_id and o.user_id=?";

        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(period.getStartDate()));
            ps.setDate(2, Date.valueOf(period.getEndDate()));
            ps.setLong(3, userId);
            ResultSet rs = ps.executeQuery();
            Map<Long, MenuOrder> orders = new HashMap<>();
            while (rs.next()) {
                Long menuId = rs.getLong("menu_id");
                Long mealId = rs.getLong("meal_id");
                Integer orderIdTemp = rs.getObject("order_id", Integer.class);
                Long orderId = orderIdTemp != null ? orderIdTemp.longValue() : notExistedOrderId;
                boolean isChosen = rs.getBoolean("is_chosen");
                String type = rs.getString("type");
                String name = rs.getString("name");
                if (!orders.containsKey(menuId)) {
                    LocalDate date = rs.getDate("date").toLocalDate();
                    List<OrderDetails> orderDetails = new ArrayList<>();
                    orderDetails.add(new OrderDetails(orderId, mealId, name, type, isChosen));
                    MenuOrder order = new MenuOrder(menuId, date, orderDetails);
                    orders.put(menuId, order);
                } else {
                    MenuOrder order = orders.get(menuId);
                    order.getOrderDetails().add(
                            new OrderDetails(orderId, mealId, name, type, isChosen));
                }
            }
            List<MenuOrder> menuOrders = new ArrayList<>(orders.values());
            menuOrders.sort(Comparator.comparing(MenuOrder::getDate));
            return menuOrders;
        });
    }

    @Override
    public List<OrderStatistic> findStatisticByDate(LocalDate date) {
        String sql = "select m.type, g.id as group_id, g.name as group_name, g.dinner_time," +
                "count(o.meal_id) over (partition by g.name)" +
                " from orders o, users u, groups g, meals m, day_menu d" +
                " where d.date = ? and m.menu_id = d.id and o.meal_id = m.id" +
                " and o.is_chosen = true and o.user_id = u.id" +
                " and u.group_id = g.id";
        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            Map<Long, OrderStatistic> statisticByGroup = new HashMap<>();
            while (rs.next()) {
                Long groupId = rs.getLong("group_id");
                String type = rs.getString("type");
                Integer count = rs.getInt("count");
                if (!statisticByGroup.containsKey(groupId)) {
                    String dinnerTime = rs.getString("dinner_time");
                    String groupName = rs.getString("group_name");
                    Map<String, Integer> countByType = new HashMap<>();
                    countByType.put(type, count);
                    statisticByGroup.put(groupId, new OrderStatistic(groupName, dinnerTime, countByType));
                } else {
                    OrderStatistic os = statisticByGroup.get(groupId);
                    os.getCountByType().put(type, count);
                }
            }
            List<OrderStatistic> statistics = new ArrayList<>(statisticByGroup.values());
            statistics.sort(Comparator.comparing(OrderStatistic::getDinnerTime));
            return statistics;
        });
    }

    @Override
    public List<GroupOrderDetails> findAllInGroup(Long groupId, LocalDate date) {
        String sql = "select u.id user_id, u.full_name, m.type, o.is_chosen" +
                "  from day_menu d" +
                "  join meals m on d.id = m.menu_id and d.date = ?" +
                "  join users u on u.group_id = ?" +
                "  left outer join orders o on m.id = o.meal_id and u.id = o.user_id" +
                "  order by u.id;";
        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(date));
            ps.setLong(2, groupId);
            ResultSet rs = ps.executeQuery();
            Map<Long, GroupOrderDetails> orderDetailsByUser = new HashMap<>();
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                String type = rs.getString("type");
                boolean isChosen = rs.getBoolean("is_chosen");
                if (!orderDetailsByUser.containsKey(userId)) {
                    String fullName = rs.getString("full_name");
                    Map<String, Boolean> chooseByType = new HashMap<>();
                    chooseByType.put(type, isChosen);
                    orderDetailsByUser.put(userId, new GroupOrderDetails(fullName, chooseByType));
                } else {
                    orderDetailsByUser.get(userId).getChooseByType().put(type, isChosen);
                }
            }
            List<GroupOrderDetails> orderDetails = new ArrayList<>(orderDetailsByUser.values());
            orderDetails.sort(Comparator.comparing(GroupOrderDetails::getFullName));
            return orderDetails;
        });
    }

    @Override
    public void clear() {
        try (Connection connection = connectionSupplier.getConnection()) {
            connection.prepareStatement("delete from orders").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(Connection connection, List<Order> orders) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into orders (user_id, meal_id, is_chosen) values (?, ?, ?)")) {

            for (Order order : orders) {
                ps.setLong(1, order.getUserId());
                ps.setLong(2, order.getMealId());
                ps.setBoolean(3, order.isChosen());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void update(Connection connection, List<Order> orders) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "update orders set is_chosen=? where id=?")) {

            for (Order order : orders) {
                ps.setBoolean(1, order.isChosen());
                ps.setLong(2, order.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public static OrderDataMapper getInstance(){
        return dataMapper;
    }
}
