package dao.impl;

import dao.client.UserDataMapper;
import domain.User;
import domain.UserRole;

import java.sql.ResultSet;

public class UserDataMapperPgImpl extends AbstractDataMapper implements UserDataMapper {

    private static UserDataMapper dataMapper = new UserDataMapperPgImpl();

    private UserDataMapperPgImpl(){}

    @Override
    public User findByUsername(String username) {
        String sql = "select * from users where username=?";
        return executeQuery(sql, ps-> {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                user.setUsername(username);
            }
            return user;
        });
    }

    @Override
    public Long create(User user) {
        String sql = "insert into users (username, password, full_name, role) values (?, ?, ?, ?) returning id";
        return executeQuery(sql, ps -> {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        });
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    public static UserDataMapper getInstance(){
        return dataMapper;
    }
}
