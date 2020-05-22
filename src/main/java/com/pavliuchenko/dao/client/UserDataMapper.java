package com.pavliuchenko.dao.client;

import com.pavliuchenko.domain.User;
import com.pavliuchenko.domain.dto.UserInfo;

public interface UserDataMapper extends DataMapper<Long, User>{
    User create(User user);

    User findByUsername(String username);

    void update(Long id, Long groupId);

    void delete(Long id);

    Page<UserInfo> searchByFullName(String fullName, int pageNumber, int size);

    Page<UserInfo> findAll(int pageNumber, int size);
}
