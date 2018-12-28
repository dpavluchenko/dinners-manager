package dao.impl;

import config.ApplicationProperties;
import dao.function.ConnectionSupplier;
import dao.function.StatementExecutor;
import dao.function.TransactionExecutor;
import exception.DataMapperException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDataMapper{
    protected final ConnectionSupplier connectionSupplier;

    protected AbstractDataMapper() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        ApplicationProperties properties = ApplicationProperties.getInstance();
        connectionSupplier = () -> DriverManager.getConnection(properties.getProperty("db.url"),
                properties.getProperty("db.user"), properties.getProperty("db.password"));
    }

    protected <T> T executeQuery(String sql, StatementExecutor<T> executor){
        try (Connection connection = connectionSupplier.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            return executor.execute(statement);
        } catch (SQLException e) {
           throw new DataMapperException(e);
        }
    }

    protected <T> T executeTransaction(TransactionExecutor<T> executor){
        try (Connection connection = connectionSupplier.getConnection()){
          try{
              connection.setAutoCommit(false);
              T result = executor.execute(connection);
              connection.commit();
              return result;
          } catch (SQLException e){
              connection.rollback();
              throw new DataMapperException(e);
          }
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

}
