package exception.controller;

public class NotFoundRequestPath extends RuntimeException{

    public NotFoundRequestPath(String message) {
        super(message);
    }
}
