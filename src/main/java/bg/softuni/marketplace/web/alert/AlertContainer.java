package bg.softuni.marketplace.web.alert;

import java.util.List;

public interface AlertContainer {

    void clear();

    boolean isEmpty();

    void info(String text);

    void success(String text);

    void error(String text);

    void add(Alert.Type type, String text);

    List<Alert> get();
}
