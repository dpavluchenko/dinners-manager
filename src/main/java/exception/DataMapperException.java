package exception;

public class DataMapperException extends RuntimeException {

    public DataMapperException(String message) {
        super(message);
    }

    public DataMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataMapperException(Throwable cause) {
        super(cause);
    }
}
