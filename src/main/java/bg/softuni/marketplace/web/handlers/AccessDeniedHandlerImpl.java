package bg.softuni.marketplace.web.handlers;

import bg.softuni.marketplace.config.WebConfig;
import lombok.extern.java.Log;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;

@Log
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            log.log(Level.WARNING, () -> String.format(
                    "Unauthorized access: {%s} attempted to access {%s}",
                    auth.getName(), request.getRequestURI()));
        }

        response.sendRedirect(request.getContextPath() + WebConfig.URL_ERROR_UNAUTHORIZED);
    }
}
