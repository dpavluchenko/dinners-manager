package dao.client;

import dao.impl.UserDataMapperPgImpl;
import domain.Entity;
import domain.User;

import java.util.NoSuchElementException;

public class SimpleDataMapperFactory {
    private SimpleDataMapperFactory() {
    }

    public static <T extends DataMapper> T getDataMapperFor(Class<? extends Entity> entity) {
      DataMapper dataMapper;
      if (entity == User.class) dataMapper = UserDataMapperPgImpl.getInstance();
      else throw new NoSuchElementException(String.format("Can't find mapper for %s", entity));
      return (T) dataMapper;
    }

}