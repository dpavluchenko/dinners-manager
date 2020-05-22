package com.pavliuchenko.dao.impl;

import com.pavliuchenko.dao.client.DayMenuDataMapper;
import com.pavliuchenko.domain.DayMenu;
import com.pavliuchenko.domain.Meal;
import com.pavliuchenko.domain.MealType;
import com.pavliuchenko.domain.Period;
import com.pavliuchenko.domain.dto.MenuDetails;
import com.pavliuchenko.exception.dao.NotFoundDataMapperException;
import com.pavliuchenko.infrastructure.annotation.Singleton;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Singleton
public class DayMenuDataMapperPgImpl extends AbstractDataMapper implements DayMenuDataMapper {

    @Override
    public DayMenu create(DayMenu menu) {
        return executeTransaction(connection -> {
            try (PreparedStatement createMenu = connection.prepareStatement("insert into day_menu (date) values (?) returning id")) {
                createMenu.setDate(1, Date.valueOf(menu.getDate()));
                ResultSet result = createMenu.executeQuery();
                result.next();
                menu.setId(result.getLong("id"));
            }
            try (PreparedStatement createMeal = connection.prepareStatement("insert into meals (name, type, menu_id) values (?, ?, ?) returning id")) {
                for (Meal meal : menu.getMeals()) {
                    createMeal.setString(1, meal.getName());
                    createMeal.setString(2, meal.getType().name());
                    createMeal.setLong(3, menu.getId());
                    ResultSet result = createMeal.executeQuery();
                    result.next();
                    meal.setId(result.getLong("id"));
                    meal.setMenuId(menu.getId());
                }
            }
            return menu;
        });
    }

    @Override
    public void update(Long id, Map<MealType, String> meals) {
        String sql = "update meals set name=? where menu_id=? and type=?";
        executeQuery(sql, ps -> {
            for (Map.Entry<MealType, String> meal : meals.entrySet()) {
                ps.setString(1, meal.getValue());
                ps.setLong(2, id);
                ps.setString(3, meal.getKey().name());
                ps.addBatch();
            }
            return ps.executeBatch();
        });
    }

    @Override
    public LocalDate delete(Long id) {
        String sql = "delete from day_menu where id=? returning date";
        return executeQuery(sql, ps -> {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate("date")
                        .toLocalDate();
            }
            throw new NotFoundDataMapperException(String.format("Menu with id %d not found", id));
        });
    }

    @Override
    public List<MenuDetails> findByDate(LocalDate date) {
        String sql = "select d.id as menu_id, d.date, m.type, m.name " +
                " from day_menu as d, meals as m" +
                " where d.date=?" +
                " and d.id=m.menu_id ";

        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            return getMenuDetails(rs);
        });
    }

    @Override
    public List<MenuDetails> findByPeriod(Period period) {
        String sql = "select d.id as menu_id, d.date, m.type, m.name " +
                " from day_menu as d, meals as m" +
                " where d.date>=?" +
                " and d.date<=?" +
                " and d.id=m.menu_id ";

        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(period.getStartDate()));
            ps.setDate(2, Date.valueOf(period.getEndDate()));
            ResultSet rs = ps.executeQuery();
            return getMenuDetails(rs);
        });
    }

    @Override
    public LocalDate findMaxMealDate() {
        return executeQuery("select max(date) date from day_menu", ps -> {
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            Date date = resultSet.getDate("date");
            if (date != null) return date.toLocalDate();
            else return null;
        });
    }

    private List<MenuDetails> getMenuDetails(ResultSet rs) throws SQLException {
        Map<Long, MenuDetails> details = new HashMap<>();
        while (rs.next()) {
            Long menuId = rs.getLong("menu_id");
            MealType type = MealType.valueOf(rs.getString("type"));
            String name = rs.getString("name");
            if (!details.containsKey(menuId)) {
                Map<MealType, String> meals = new HashMap<>();
                meals.put(type, name);
                LocalDate date = rs.getDate("date").toLocalDate();
                MenuDetails menuDetails = new MenuDetails(menuId, date, meals);
                details.put(menuId, menuDetails);
            } else {
                MenuDetails menuDetails = details.get(menuId);
                menuDetails.getMeals().put(type, name);
            }
        }
        List<MenuDetails> menuDetails = new ArrayList<>(details.values());
        menuDetails.sort(Comparator.comparing(MenuDetails::getDate));
        return menuDetails;
    }

}
