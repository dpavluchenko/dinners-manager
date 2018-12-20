package dao.client;

import domain.Entity;

public interface DataMapper<ID extends Number, T extends Entity> {
    ID create(T entity);
    T findById(ID id);
}
