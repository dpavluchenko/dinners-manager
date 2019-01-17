package controller.binder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.binder.DataReadException;
import exception.binder.DataWriteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HttpDataBinder {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T getModelFromRequest(HttpServletRequest request, Class<T> dataClass) {
        return getData(request, mapper.constructType(dataClass));
    }

    public static <T> List<T> getListOfModelsFromRequest(HttpServletRequest request, Class<T> dataClass) {
        return getData(request, mapper.getTypeFactory().constructCollectionType(List.class, dataClass));
    }

    public static void writeDataToResponse(Object data, HttpServletResponse response) {
        response.setContentType("application/json");
        try {
            response.getWriter()
                    .write(
                            mapper
                                    .writeValueAsString(data));
        } catch (IOException e) {
            throw new DataReadException(e);
        }
    }

    private static <T> T getData(HttpServletRequest request, JavaType type) {
        try {
            return mapper.readValue(request.getReader(), type);
        } catch (IOException e) {
            throw new DataWriteException(e);
        }
    }
}
