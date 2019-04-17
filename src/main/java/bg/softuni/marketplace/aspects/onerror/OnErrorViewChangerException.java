package bg.softuni.marketplace.aspects.onerror;

class OnErrorViewChangerException extends RuntimeException {

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    OnErrorViewChangerException(String message) {
        super(message, null, true, false);
    }
}
