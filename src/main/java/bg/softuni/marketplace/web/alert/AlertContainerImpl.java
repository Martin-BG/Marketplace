package bg.softuni.marketplace.web.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Session scoped {@link AlertContainer} implementation.
 */
@RequiredArgsConstructor
@Component
@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
public class AlertContainerImpl implements AlertContainer, Serializable {

    private static final long serialVersionUID = 1L;

    private final LinkedList<Alert> alerts = new LinkedList<>();

    @Override
    public void clear() {
        alerts.clear();
    }

    @Override
    public boolean isEmpty() {
        return alerts.isEmpty();
    }

    @Override
    public void addInfo(String text) {
        add(AlertType.INFO, text);
    }

    @Override
    public void addSuccess(String text) {
        add(AlertType.SUCCESS, text);
    }

    @Override
    public void addError(String text) {
        add(AlertType.ERROR, text);
    }

    @Override
    public void add(AlertType type, String text) {
        Alert alert;

        switch (type) {
        case INFO:
            alert = Alert.info(text);
            break;
        case SUCCESS:
            alert = Alert.success(text);
            break;
        case ERROR:
            alert = Alert.error(text);
            break;
        default:
            throw new IllegalArgumentException("Unsupported or invalid AlertType: " + type);
        }

        alerts.addFirst(alert);
    }

    @Override
    public List<Alert> get() {
        return Collections.unmodifiableList(alerts);
    }
}
