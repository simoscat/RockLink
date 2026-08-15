package exception;

public class ControllerLogicException extends RuntimeException {
    public ControllerLogicException(String message) {
        super(message);
    }
    public ControllerLogicException(String message, Throwable cause){
        super(message, cause);
    }
}
