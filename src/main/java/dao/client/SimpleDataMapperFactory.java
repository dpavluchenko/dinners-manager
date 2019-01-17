package dao.client;

import dao.impl.DayMenuDataMapperPgImpl;
import dao.impl.GroupDataMapperPgImpl;
import dao.impl.OrderDataMapperPgImpl;
import dao.impl.UserDataMapperPgImpl;

import java.util.NoSuchElementException;

public class SimpleDataMapperFactory {
    private SimpleDataMapperFactory() {
    }

    public static <T extends DataMapper> T getDataMapperFor(Class<T> mapper) {
      DataMapper dataMapper;
      if (mapper == UserDataMapper.class) dataMapper = UserDataMapperPgImpl.getInstance();
      else if (mapper == DayMenuDataMapper.class) dataMapper = DayMenuDataMapperPgImpl.getInstance();
      else if (mapper == GroupDataMapper.class) dataMapper = GroupDataMapperPgImpl.getInstance();
      else if (mapper == OrderDataMapper.class) dataMapper = OrderDataMapperPgImpl.getInstance();
      else throw new NoSuchElementException(String.format("Can't find mapper for %s", mapper));
      return (T) dataMapper;
    }

}