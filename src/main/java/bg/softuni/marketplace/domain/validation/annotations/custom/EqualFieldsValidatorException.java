package bg.softuni.marketplace.domain.validation.annotations.custom;

import java.io.Serial;

public class EqualFieldsValidatorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    public EqualFieldsValidatorException(String message) {
        super(message, null, true, false);
    }

    public EqualFieldsValidatorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
