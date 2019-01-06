package dao.impl;

import dao.client.MealDataMapper;
import domain.Meal;
import domain.MealType;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class MealDataMapperPgImpl extends AbstractDataMapper implements MealDataMapper {

    private static MealDataMapper dataMapper = new MealDataMapperPgImpl();

    private MealDataMapperPgImpl(){}

    @Override
    public Meal create(Meal meal) {
        String sql = "insert into meals (name, date, type) values (?, ?, ?) returning id";
        Long id = executeQuery(sql, ps -> {
            ps.setString(1, meal.getName());
            ps.setDate(2, Date.valueOf(meal.getDate()));
            ps.setString(3, meal.getType().name());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
        meal.setId(id);
        return meal;
    }

    @Override
    public LocalDate findMaxMealDate() {
        String sql = "select max(date) from meals";
        return executeQuery(sql, ps -> {
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next())
            return resultSet.getDate("max").toLocalDate();
            else return null;
        });
    }

    @Override
    public List<Meal> create(List<Meal> meals) {
        return null;
    }

    @Override
    public Meal findById(Long id) {
        String sql = "select * from meals where id=?";
        return executeQuery(sql, ps-> {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Meal meal = new Meal();
            if (rs.next()) {
                meal.setId(rs.getLong("id"));
                meal.setName(rs.getString("name"));
                meal.setType(MealType.valueOf(rs.getString("type")));
                meal.setDate(rs.getDate("date").toLocalDate());
            }
            return meal;
        });
    }

    public static MealDataMapper getInstance(){
        return dataMapper;
    }
}
