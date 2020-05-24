package com.pavliuchenko.dao.impl;

import com.pavliuchenko.dao.client.GroupDataMapper;
import com.pavliuchenko.dao.source.DatabaseSource;
import com.pavliuchenko.domain.Group;
import com.pavliuchenko.infrastructure.annotation.InjectType;
import com.pavliuchenko.infrastructure.annotation.Property;
import com.pavliuchenko.infrastructure.annotation.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
public class GroupDataMapperPgImpl implements GroupDataMapper {

    @InjectType
    private DatabaseSource databaseSource;

    @Property("group.init.id")
    private Long noneGroupId;

    @Property("group.init.name")
    private String noneGroupName;

    @Override
    public void init() {
        String sql = "insert into groups (id, dinner_time, name, is_default)" +
                "values (?, '', ?, false)" +
                "on conflict (id)  do nothing ";
        databaseSource.executeQuery(sql, ps -> {
            ps.setLong(1, noneGroupId);
            ps.setString(2, noneGroupName);
            return ps.execute();
        });
    }

    @Override
    public void setDefaultGroup(Long groupId) {
        String sql = "update groups set is_default = case" +
                "                  when id = ? then true" +
                "                  else false" +
                "  end";
        databaseSource.executeQuery(sql, ps -> {
            ps.setLong(1, groupId);
            return ps.execute();
        });
    }

    @Override
    public Long getDefaultGroupId() {
        Long id = databaseSource.executeQuery("select id from groups where is_default=true", ps -> {
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
        String sql = "insert into groups (dinner_time, name, is_default) values (?, ?, ?) returning id";
        Long id = databaseSource.executeQuery(sql, ps -> {
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
        databaseSource.executeQuery("update groups set name = ?,  dinner_time = ? where id = ?", ps -> {
            ps.setString(1, group.getName());
            ps.setString(2, group.getDinnerTime());
            ps.setLong(3, group.getId());
            return ps.execute();
        });
    }

    @Override
    public void delete(Long id) {
        databaseSource.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("update users set group_id = ? where group_id = ?")) {
                ps.setLong(1, noneGroupId);
                ps.setLong(2, id);
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("delete from groups where id = ?")) {
                ps.setLong(1, id);
                ps.execute();
            }
            return id;
        });
    }

    @Override
    public List<Group> findAll() {
        return databaseSource.executeQuery("select * from groups where id != ?", ps -> {
            ps.setLong(1, noneGroupId);
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

}
