package bg.softuni.marketplace.aspects.validate;

public class ValidateMethodArgumentsException extends RuntimeException {

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    public ValidateMethodArgumentsException(String message) {
        super(message, null, true, false);
    }
}
