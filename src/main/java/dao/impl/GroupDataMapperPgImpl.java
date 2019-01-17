package dao.impl;

import dao.client.GroupDataMapper;
import domain.Group;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroupDataMapperPgImpl extends AbstractDataMapper implements GroupDataMapper {

    private static GroupDataMapper dataMapper = new GroupDataMapperPgImpl();

    private final Long noneGroupId = -1L;

    private GroupDataMapperPgImpl() {
    }

    @Override
    public void setDefaultGroup(Long groupId) {
        String sql = "update groups set is_default = case" +
                "                  when id = ? then true" +
                "                  else false" +
                "  end";
        executeQuery(sql, ps -> {
            ps.setLong(1, groupId);
            return ps.execute();
        });
    }

    @Override
    public Long getDefaultGroupId() {
        Long id = executeQuery("select id from groups where is_default=true", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
            return null;
        });
        return id != null ? id : noneGroupId;
    }

    @Override
    public Group create(Group group) {
        String sql = "insert into groups (time, name, is_default) values (?, ?, ?) returning id";
        Long id = executeQuery(sql, ps -> {
            ps.setString(1, group.getDinnerTime());
            ps.setString(2, group.getName());
            ps.setBoolean(3, group.isDefault());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
        group.setId(id);
        return group;
    }

    @Override
    public void update(Group group) {
        executeQuery("update groups set name = ?,  dinner_time = ? where id = ?", ps -> {
            ps.setString(1, group.getName());
            ps.setString(2, group.getDinnerTime());
            ps.setLong(3, group.getId());
            return ps.execute();
        });
    }

    @Override
    public void delete(Long id) {
        executeQuery("delete from groups where id = ?", ps -> {
            ps.setLong(1, id);
            return ps.execute();
        });
    }

    @Override
    public List<Group> findAll() {
        return executeQuery("select * from groups", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Group> groups = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String dinnerTime = rs.getString("dinner_time");
                boolean isDefault = rs.getBoolean("is_default");
                Group group = new Group(name, dinnerTime, isDefault);
                group.setId(id);
                groups.add(group);
            }
            groups.sort(Comparator.comparing(Group::getDinnerTime));
            return groups;
        });
    }


    public static GroupDataMapper getInstance() {
        return dataMapper;
    }
}
