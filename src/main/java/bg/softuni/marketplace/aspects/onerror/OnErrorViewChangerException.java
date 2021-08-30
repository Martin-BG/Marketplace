package bg.softuni.marketplace.aspects.onerror;

import java.io.Serial;

class OnErrorViewChangerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lightweight exception - stacktrace disabled, suppression enabled
     */
    OnErrorViewChangerException(String message) {
        super(message, null, true, false);
    }
}
