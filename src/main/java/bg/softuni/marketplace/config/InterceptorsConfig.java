package bg.softuni.marketplace.config;

import bg.softuni.marketplace.web.interceptors.ThymeleafLayoutInterceptor;
import bg.softuni.marketplace.web.interceptors.TitleInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class InterceptorsConfig {

    public static final String VIEW_ATTRIBUTE_NAME = "view";
    public static final String TITLE_ATTRIBUTE_NAME = "title";
    public static final String ALERT_ATTRIBUTE_NAME = "alert";

    @Bean
    public HandlerInterceptor thymeleafLayoutInterceptor() {
        return ThymeleafLayoutInterceptor
                .builder()
                .withDefaultLayout("/layouts/default")
                .withViewAttribute(VIEW_ATTRIBUTE_NAME)
                .withViewPrefix("/views/")
                .build();
    }

    @Bean
    public HandlerInterceptor titleInterceptor(MessageSource messageSource) {
        return TitleInterceptor
                .builder(messageSource)
                .withTitleCode("application.title")
                .withTitleAttribute(TITLE_ATTRIBUTE_NAME)
                .build();
    }
}
