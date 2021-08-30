package bg.softuni.marketplace.aspects.validate;

import java.io.Serial;

class ValidateMethodArgumentsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    ValidateMethodArgumentsException(String message) {
        super(message, null, true, false);
    }
}
