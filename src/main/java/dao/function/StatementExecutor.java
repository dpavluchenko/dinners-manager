package dao.function;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementExecutor<T> {
    T execute(PreparedStatement statement) throws SQLException;
}
