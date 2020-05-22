package com.pavliuchenko.dao.impl;

import com.pavliuchenko.exception.dao.AlreadyExistDataMapperException;
import com.pavliuchenko.exception.dao.DataMapperException;

import java.sql.SQLException;

public class ExceptionHelper {

    public static DataMapperException getEx(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new AlreadyExistDataMapperException("Duplication of data", e);
        }
        return new DataMapperException(e);
    }
}
