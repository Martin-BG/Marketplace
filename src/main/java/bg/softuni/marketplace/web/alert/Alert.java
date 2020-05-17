package bg.softuni.marketplace.web.alert;

import lombok.Getter;

import java.io.Serializable;

import static bg.softuni.marketplace.web.alert.Alert.Type.*;

/**
 * Immutable alert class.
 *
 * @see Alert.Type
 * @see Alert.Type#with(String)
 */
@Getter
public final class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Type type;
    private final String text;

    private Alert(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public boolean isInfo() {
        return type == INFO;
    }

    public boolean isSuccess() {
        return type == SUCCESS;
    }

    public boolean isError() {
        return type == ERROR;
    }

    /**
     * Alert types.
     *
     * @see Type#with(String)
     */
    public enum Type {
        INFO, SUCCESS, ERROR;

        /**
         * Creates a new {@link Alert} instance of this {@link Type}
         * from the {@code text}
         *
         * @param text text of the {@link Alert}
         * @return a new {@link Alert} instance
         */
        public Alert with(String text) {
            return new Alert(this, text);
        }
    }
}
