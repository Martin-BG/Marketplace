package bg.softuni.marketplace.web.common;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MessageHelper {

    private final MessageSource messageSource;

    public String getLocalizedMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
