package com.pavliuchenko.controller.handler;

import com.pavliuchenko.controller.binder.HttpDataBinder;
import com.pavliuchenko.exception.binder.DataReadException;
import com.pavliuchenko.exception.binder.DataWriteException;
import com.pavliuchenko.exception.dao.AlreadyExistDataMapperException;
import com.pavliuchenko.exception.dao.DataMapperException;
import com.pavliuchenko.exception.dao.NotFoundDataMapperException;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

@Log
public class ExceptionHandler {

    public static void handle(HttpServletResponse response, Exception e) {
        Class exClass = e.getClass();
        String error;
        if (exClass == AlreadyExistDataMapperException.class) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            error = "Такая запись уже существует!";
        } else if (exClass == NotFoundDataMapperException.class) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            error = "Такой записи не существует!";
        } else if (exClass == DataMapperException.class) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            error = "Ошибка чтения данных";
        } else if (exClass == DataReadException.class || exClass == IllegalArgumentException.class) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            error = "Неверные параметры запроса";
        } else if (exClass == DataWriteException.class) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            error = "Ошибка обработки данных";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            error = "Непредвиденная ошибка";
        }
        log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        HttpDataBinder.writeDataToResponse(error, response);
    }
}
