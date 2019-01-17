package exception.binder;

import java.io.IOException;

public class DataReadException extends RuntimeException{

    public DataReadException(IOException e) {
        super(e);
    }
}
