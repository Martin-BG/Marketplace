package bg.softuni.marketplace.web.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static bg.softuni.marketplace.web.alert.Alert.Type.*;

/**
 * Session scoped {@link AlertContainer} implementation.
 */
@RequiredArgsConstructor
@Component
@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
public class AlertContainerImpl implements AlertContainer, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LinkedList<Alert> alerts = new LinkedList<>();

    @Override
    public void clear() {
        synchronized (alerts) {
            alerts.clear();
        }
    }

    @Override
    public boolean isEmpty() {
        return alerts.isEmpty();
    }

    @Override
    public void info(String text) {
        add(INFO, text);
    }

    @Override
    public void success(String text) {
        add(SUCCESS, text);
    }

    @Override
    public void error(String text) {
        add(ERROR, text);
    }

    @Override
    public void add(Alert.Type alertType, String text) {
        Alert alert = alertType.with(text);

        synchronized (alerts) {
            alerts.addFirst(alert);
        }
    }

    @Override
    public List<Alert> get() {
        return Collections.unmodifiableList(alerts);
    }
}
