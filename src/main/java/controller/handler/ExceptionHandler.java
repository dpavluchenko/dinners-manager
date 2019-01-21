package controller.handler;

import exception.binder.DataReadException;
import exception.binder.DataWriteException;
import exception.dao.AlreadyExistDataMapperException;
import exception.dao.DataMapperException;
import exception.dao.NotFoundDataMapperException;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

@Log
public class ExceptionHandler {

    public static void handle(HttpServletResponse response, Exception e) {
        Class exClass = e.getClass();
        if (exClass == AlreadyExistDataMapperException.class) response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Такая запись уже существует!");
        else if (exClass == NotFoundDataMapperException.class) response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Такой записи не существует!");
        else if (exClass == DataMapperException.class) response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка чтения данных");
        else if (exClass == DataReadException.class || exClass == IllegalArgumentException.class) response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Неверные параметры запроса");
        else if (exClass == DataWriteException.class) response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        else response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Непредвиденная ошибка");
        log.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
}
