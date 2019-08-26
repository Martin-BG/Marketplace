package bg.softuni.marketplace.service.exception;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException() {
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    public IdNotFoundException(String message) {
        super(message, null, true, false);
    }
}
