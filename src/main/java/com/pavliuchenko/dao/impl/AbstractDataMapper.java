package com.pavliuchenko.dao.impl;

import com.pavliuchenko.dao.function.ConnectionSupplier;
import com.pavliuchenko.dao.function.StatementExecutor;
import com.pavliuchenko.dao.function.TransactionExecutor;
import com.pavliuchenko.infrastructure.annotation.Property;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDataMapper{
    protected final ConnectionSupplier connectionSupplier;

    @Property("db.driver")
    protected String dbDriver;
    @Property("db.url")
    protected String dbUrl;
    @Property("db.user")
    protected String dbUser;
    @Property("db.password")
    protected String dbPassword;

    @SneakyThrows
    protected AbstractDataMapper() {
        Class.forName(dbDriver);
        connectionSupplier = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    protected <T> T executeQuery(String sql, StatementExecutor<T> executor){
        try (Connection connection = connectionSupplier.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            return executor.execute(statement);
        } catch (SQLException e) {
            throw ExceptionHelper.getEx(e);
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
              throw ExceptionHelper.getEx(e);
          }
        } catch (SQLException e) {
            throw ExceptionHelper.getEx(e);
        }
    }

}
