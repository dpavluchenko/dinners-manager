package exception.binder;

import java.io.IOException;

public class DataWriteException extends RuntimeException{

    public DataWriteException(IOException e) {
        super(e);
    }
}
