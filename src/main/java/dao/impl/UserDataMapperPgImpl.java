package dao.impl;

import dao.client.Page;
import dao.client.UserDataMapper;
import domain.User;
import domain.UserRole;
import domain.dto.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDataMapperPgImpl extends AbstractDataMapper implements UserDataMapper {

    private static UserDataMapper dataMapper = new UserDataMapperPgImpl();

    private UserDataMapperPgImpl() {
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select * from users where username=?";
        return executeQuery(sql, ps -> {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                user.setUsername(username);
                user.setGroupId(rs.getLong("group_id"));
            }
            return user;
        });
    }

    @Override
    public void update(Long id, Long groupId) {
        executeQuery("update users set group_id = ? where id = ?", ps -> {
            ps.setLong(1, groupId);
            ps.setLong(2, id);
            return ps.execute();
        });
    }

    @Override
    public void delete(Long id) {
        executeQuery("delete from users where id = ?", ps -> {
            ps.setLong(1, id);
            return ps.execute();
        });
    }

    @Override
    public Page<UserInfo> searchByFullName(String fullName, int pageNumber, int size) {
        int offset = pageNumber * size;
        String sql = "select u.id, u.full_name, g.dinner_time, g.name as group_name" +
                " from users u, groups g where u.group_id = g.id" +
                " and full_name ILIKE ?" +
                " order by u.full_name";

        String regex = '%' + fullName + '%';

        return executeTransaction(connection -> {
            String selectSql = "select u.id, u.full_name, g.dinner_time, g.name as group_name" +
                    " from users u, groups g where u.group_id = g.id" +
                    " and full_name ILIKE ?" +
                    " order by u.full_name limit ? offset ?";

            String countSql = "select count(id) from users where full_name ILIKE ?";

            Page<UserInfo> page = new Page<>(pageNumber, size);

            try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                ps.setString(1, regex);
                ps.setInt(2, size);
                ps.setInt(3, offset);
                ResultSet rs = ps.executeQuery();
                List<UserInfo> users = getUserInfos(rs);
                page.setItems(users);
            }

            if (page.getItems().size() >= size) {
                page.setTotalPages(countTotalPages(connection, countSql, size));
            } else {
                page.setTotalPages((int) Math.ceil((double) page.getItems().size() / size));
            }
            return page;
        });
    }

    @Override
    public Page<UserInfo> findAll(int pageNumber, int size) {
        int offset = pageNumber * size;
        return executeTransaction(connection -> {
            String selectSql = "select u.id, u.full_name, g.dinner_time, g.name as group_name from users u, groups g" +
                    " where u.group_id = g.id" +
                    " order by u.full_name limit ? offset ?";

            String countSql = "select count(id) from users";

            Page<UserInfo> page = new Page<>(pageNumber, size);

            try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                ps.setInt(1, size);
                ps.setInt(2, offset);
                ResultSet rs = ps.executeQuery();
                List<UserInfo> users = getUserInfos(rs);
                page.setItems(users);
            }
            page.setTotalPages(countTotalPages(connection, countSql, size));
            return page;
        });
    }

    @Override
    public User create(User user) {
        String sql = "insert into users (username, password, full_name, role, group_id) values (?, ?, ?, ?, ?) returning id";
        Long id = executeQuery(sql, ps -> {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ps.setLong(5, user.getGroupId());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
        user.setId(id);
        return user;
    }

    private List<UserInfo> getUserInfos(ResultSet rs) throws SQLException {
        List<UserInfo> users = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            String groupName = rs.getString("group_name");
            String dinnerTime = rs.getString("dinner_time");
            users.add(new UserInfo(id, fullName, groupName, dinnerTime));
        }
        return users;
    }

    private int countTotalPages(Connection connection, String countSql, int size) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(countSql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            double totalUsers = rs.getDouble("count");
            return (int) Math.ceil(totalUsers / size);
        }
    }

    public static UserDataMapper getInstance() {
        return dataMapper;
    }
}
