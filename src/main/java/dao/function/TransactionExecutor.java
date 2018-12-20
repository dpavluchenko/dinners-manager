package dao.function;

import java.sql.Connection;
import java.sql.SQLException;


public interface TransactionExecutor<T> {
    T execute(Connection connection) throws SQLException;
}
