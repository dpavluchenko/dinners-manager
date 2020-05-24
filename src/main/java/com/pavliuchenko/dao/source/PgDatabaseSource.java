package com.pavliuchenko.dao.source;

import com.pavliuchenko.dao.function.StatementExecutor;
import com.pavliuchenko.dao.function.TransactionExecutor;
import com.pavliuchenko.exception.dao.ExceptionHelper;
import com.pavliuchenko.infrastructure.annotation.Property;
import com.pavliuchenko.infrastructure.annotation.Singleton;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
public class PgDatabaseSource implements DatabaseSource {

    @Property("db.driver")
    protected String dbDriver;
    @Property("db.url")
    protected String dbUrl;
    @Property("db.user")
    protected String dbUser;
    @Property("db.password")
    protected String dbPassword;

    @SneakyThrows
    @PostConstruct
    private void init() {
        Class.forName(dbDriver);
    }

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public <T> T executeQuery(String sql, StatementExecutor<T> executor) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            return executor.execute(statement);
        } catch (SQLException e) {
            throw ExceptionHelper.getEx(e);
        }
    }

    @Override
    public <T> T executeTransaction(TransactionExecutor<T> executor) {
        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                T result = executor.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw ExceptionHelper.getEx(e);
            }
        } catch (SQLException e) {
            throw ExceptionHelper.getEx(e);
        }
    }
}
