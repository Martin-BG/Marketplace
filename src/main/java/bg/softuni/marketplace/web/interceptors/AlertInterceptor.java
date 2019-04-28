package bg.softuni.marketplace.web.interceptors;

import bg.softuni.marketplace.web.alert.Alert;
import bg.softuni.marketplace.web.alert.AlertContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static bg.softuni.marketplace.config.InterceptorsConfig.ALERT_ATTRIBUTE_NAME;

/**
 * Add all {@link Alert}s from session's {@link AlertContainer} to model.
 */

@Component
@RequiredArgsConstructor
public final class AlertInterceptor extends HandlerInterceptorAdapter {

    private final AlertContainer alertContainer;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod) {
            String originalViewName = modelAndView.getViewName();

            if (originalViewName == null || Helper.isRedirectOrForward(originalViewName)) {
                return;
            }

            if (alertContainer.isEmpty()) {
                return;
            }

            List<Alert> alerts = new ArrayList<>(alertContainer.get());

            modelAndView.addObject(ALERT_ATTRIBUTE_NAME, alerts);

            alertContainer.clear();
        }
    }
}
