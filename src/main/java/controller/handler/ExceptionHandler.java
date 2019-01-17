package controller.handler;

import lombok.extern.java.Log;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

@Log
public class ExceptionHandler {

    public static void handle(HttpServletResponse response, Exception e) {
        log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        response.setStatus(500);
    }
}
