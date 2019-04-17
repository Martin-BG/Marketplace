package bg.softuni.marketplace.config;

import bg.softuni.marketplace.web.interceptors.ThymeleafLayoutInterceptor;
import bg.softuni.marketplace.web.interceptors.TitleInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class InterceptorsConfig {

    @Bean
    public HandlerInterceptor thymeleafLayoutInterceptor() {
        return ThymeleafLayoutInterceptor
                .builder()
                .withDefaultLayout("/layouts/default")
                .withViewAttribute("view")
                .withViewPrefix("/views/")
                .build();
    }

    @Bean
    public HandlerInterceptor titleInterceptor(MessageSource messageSource) {
        return TitleInterceptor
                .builder(messageSource)
                .withTitleCode("application.title")
                .withTitleAttribute("title")
                .build();
    }
}
