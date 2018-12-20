package dao.client;

import domain.User;

public interface UserDataMapper extends DataMapper<Long, User>{
    User findByUsername(String username);
}
