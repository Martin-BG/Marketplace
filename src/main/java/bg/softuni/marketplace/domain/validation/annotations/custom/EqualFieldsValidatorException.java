package bg.softuni.marketplace.domain.validation.annotations.custom;

public class EqualFieldsValidatorException extends RuntimeException {

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
