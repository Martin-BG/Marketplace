package bg.softuni.marketplace.web.interceptors;

import bg.softuni.marketplace.web.alert.Alert;
import bg.softuni.marketplace.web.alert.AlertContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static bg.softuni.marketplace.config.InterceptorsConfig.ALERT_ATTRIBUTE_NAME;

/**
 * Add all {@link Alert}s from session's {@link AlertContainer} to model.
 */

@Component
@RequiredArgsConstructor
public final class AlertInterceptor implements HandlerInterceptor {

    private final AlertContainer alertContainer;

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           @Nullable ModelAndView modelAndView) {
        if (alertContainer.isEmpty()) {
            return;
        }

        if (handler instanceof HandlerMethod) {
            if (modelAndView == null || !modelAndView.hasView()) {
                return;
            }

            String originalViewName = modelAndView.getViewName();

            if (originalViewName == null || Helper.isRedirectOrForward(originalViewName)) {
                return;
            }

            List<Alert> alerts = new ArrayList<>(alertContainer.get());

            modelAndView.addObject(ALERT_ATTRIBUTE_NAME, alerts);

            alertContainer.clear();
        }
    }
}
