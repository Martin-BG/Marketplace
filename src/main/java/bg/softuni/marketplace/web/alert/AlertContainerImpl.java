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
        alerts.addFirst(Alert.info(text));
    }

    @Override
    public void addSuccess(String text) {
        alerts.addFirst(Alert.success(text));
    }

    @Override
    public void addError(String text) {
        alerts.addFirst(Alert.error(text));
    }

    @Override
    public void add(AlertType type, String text) {
        switch (type) {
        case INFO:
            addInfo(text);
            break;
        case SUCCESS:
            addSuccess(text);
            break;
        case ERROR:
            addError(text);
            break;
        default:
            throw new IllegalArgumentException("Unsupported or invalid AlertType: " + type);
        }
    }

    @Override
    public List<Alert> get() {
        return Collections.unmodifiableList(alerts);
    }
}
