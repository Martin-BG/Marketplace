package bg.softuni.marketplace.aspects.validate;

class ValidateMethodArgumentsException extends RuntimeException {

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    ValidateMethodArgumentsException(String message) {
        super(message, null, true, false);
    }
}
