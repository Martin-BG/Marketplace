package bg.softuni.marketplace.web.alert;

import java.util.List;

public interface AlertContainer {

    void clear();

    boolean isEmpty();

    void addInfo(String text);

    void addSuccess(String text);

    void addError(String text);

    void add(AlertType type, String text);

    List<Alert> get();
}
