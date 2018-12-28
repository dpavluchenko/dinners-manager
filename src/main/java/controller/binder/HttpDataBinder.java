package controller.binder;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpDataBinder {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T getDataFromRequest(HttpServletRequest request, Class<T> dataClass) {
        try {
            return mapper.readValue(request.getReader(), dataClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeDataToResponse(Object data, HttpServletResponse response) {
        response.setContentType("application/json");
        try {
            response.getWriter()
                    .write(
                            mapper
                                    .writeValueAsString(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
