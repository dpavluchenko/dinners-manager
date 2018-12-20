package dao.impl;

import dao.client.UserDataMapper;
import domain.User;

public class UserDataMapperPgImpl extends AbstractDataMapper implements UserDataMapper {

    private static UserDataMapper dataMapper = new UserDataMapperPgImpl();

    private UserDataMapperPgImpl(){}

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public Long create(User user) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    public static UserDataMapper getInstance(){
        return dataMapper;
    }
}
