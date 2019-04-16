package bg.softuni.marketplace.domain.validation.annotations.custom;

public class UniqueValidatorException extends RuntimeException {

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
