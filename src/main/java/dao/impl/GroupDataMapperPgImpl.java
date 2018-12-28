package dao.impl;

import dao.client.GroupDataMapper;
import domain.Group;

import java.sql.ResultSet;

public class GroupDataMapperPgImpl extends AbstractDataMapper implements GroupDataMapper {

    private static GroupDataMapper dataMapper = new GroupDataMapperPgImpl();

    private GroupDataMapperPgImpl(){}

    @Override
    public Long create(Group group) {
        String sql = "insert into groups (time, name) values (?, ?) returning id";
        return executeQuery(sql, ps -> {
            ps.setString(1, group.getDinnerTime());
            ps.setString(2, group.getName());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
    }

    @Override
    public Group findById(Long id) {
        String sql = "select * from groups where id=?";
        return executeQuery(sql, ps-> {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Group group = new Group();
            if (rs.next()) {
                group.setId(rs.getLong("id"));
                group.setDinnerTime(rs.getString("time"));
                group.setName(rs.getString("name"));
            }
            return group;
        });
    }

    public static GroupDataMapper getInstance(){
        return dataMapper;
    }
}
