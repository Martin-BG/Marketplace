package bg.softuni.marketplace.domain.validation.annotations.custom;

import java.io.Serial;

public class UniqueValidatorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    public UniqueValidatorException(String message) {
        super(message, null, true, false);
    }

    public UniqueValidatorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
