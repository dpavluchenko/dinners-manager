package com.pavliuchenko.dao.client;

import com.pavliuchenko.domain.Group;

import java.util.List;

public interface GroupDataMapper extends DataMapper<Long, Group>{
    void init();

    void setDefaultGroup(Long groupId);

    Long getDefaultGroupId();

    Group create(Group group);

    void update(Group group);

    void delete(Long id);

    List<Group> findAll();
}
