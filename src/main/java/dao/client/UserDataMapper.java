package dao.client;

import domain.User;
import domain.dto.UserInfo;

import java.util.List;

public interface UserDataMapper extends DataMapper<Long, User>{
    User create(User user);

    User findByUsername(String username);

    void update(Long id, Long groupId);

    void delete(Long id);

    List<UserInfo> searchByFullName(String fullName);

    Page<UserInfo> findAll(int pageNumber, int size);
}
