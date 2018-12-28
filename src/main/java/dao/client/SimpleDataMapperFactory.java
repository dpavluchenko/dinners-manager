package dao.client;

import dao.impl.MealDataMapperPgImpl;
import dao.impl.UserDataMapperPgImpl;

import java.util.NoSuchElementException;

public class SimpleDataMapperFactory {
    private SimpleDataMapperFactory() {
    }

    public static <T extends DataMapper> T getDataMapperFor(Class<T> mapper) {
      DataMapper dataMapper;
      if (mapper == UserDataMapper.class) dataMapper = UserDataMapperPgImpl.getInstance();
      else if (mapper == MealDataMapper.class) dataMapper = MealDataMapperPgImpl.getInstance();
      else throw new NoSuchElementException(String.format("Can't find mapper for %s", mapper));
      return (T) dataMapper;
    }

}