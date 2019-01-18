package exception.binder;

import java.io.IOException;

public class DataReadException extends RuntimeException{

    public DataReadException(IOException e) {
        this(null, e);
    }

    public DataReadException(String message) {
        this(message, null);
    }

    public DataReadException(String message, IOException e) {
        super(message, e);
    }
}
