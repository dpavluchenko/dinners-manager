package dao.client;

import domain.Group;

import java.util.List;

public interface GroupDataMapper extends DataMapper<Long, Group>{
    void setDefaultGroup(Long groupId);

    Long getDefaultGroupId();

    Group create(Group group);

    void update(Group group);

    void delete(Long id);

    List<Group> findAll();
}
