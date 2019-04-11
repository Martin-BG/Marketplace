package bg.softuni.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String URL_INDEX = "/";
    public static final String URL_ERROR_UNAUTHORIZED = "/unauthorized";

    private static final String USER_BASE_URL = "/user";
    public static final String URL_USER_ALL = USER_BASE_URL;
    public static final String URL_USER_REGISTER = USER_BASE_URL + "/register";
    public static final String URL_USER_LOGIN = USER_BASE_URL + "/login";
    public static final String URL_USER_LOGOUT = USER_BASE_URL + "/logout";
    public static final String URL_USER_HOME = USER_BASE_URL + "/home";

    private static final String API_BASE_URL = "/api";

    private final HandlerInterceptor thymeleafLayoutInterceptor;
    private final HandlerInterceptor titleInterceptor;

    @Autowired
    public WebConfig(@Qualifier("thymeleafLayoutInterceptor") HandlerInterceptor thymeleafLayoutInterceptor,
                     @Qualifier("titleInterceptor") HandlerInterceptor titleInterceptor) {
        this.thymeleafLayoutInterceptor = thymeleafLayoutInterceptor;
        this.titleInterceptor = titleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(thymeleafLayoutInterceptor);
        registry.addInterceptor(titleInterceptor);
    }
}
