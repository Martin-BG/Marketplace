package bg.softuni.marketplace.web.alert;

import lombok.Getter;

import java.io.Serializable;

/**
 * Immutable alert class with static constructors
 */
@Getter
public final class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AlertType type;
    private final String text;

    private Alert(AlertType type, String text) {
        this.type = type;
        this.text = text;
    }

    public static Alert info(String text) {
        return new Alert(AlertType.INFO, text);
    }

    public static Alert success(String text) {
        return new Alert(AlertType.SUCCESS, text);
    }

    public static Alert error(String text) {
        return new Alert(AlertType.ERROR, text);
    }

    public boolean isInfo() {
        return type == AlertType.INFO;
    }

    public boolean isSuccess() {
        return type == AlertType.SUCCESS;
    }

    public boolean isError() {
        return type == AlertType.ERROR;
    }
}
