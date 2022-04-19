package bg.softuni.marketplace.service.exception;

import java.io.Serial;

public class IdNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public IdNotFoundException() {
        super();
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
