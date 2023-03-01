package bg.softuni.marketplace.web.annotations;

import bg.softuni.marketplace.web.interceptors.TitleInterceptor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Page title suffix code (defined in *.properties file) specifier (default) or replace default title.
 * <p>
 * Can be placed on Controller methods.
 * <p>
 * Title is modified by {@link TitleInterceptor}
 * <hr>
 * Sample html:
 * <pre>{@code <head>
 *     ...
 *     <title th:text="${title}"/>
 *     ...
 * </head>}</pre>
 * Sample controller method:
 * <pre>{@code @Title("nav.login")
 * @GetMapping
 * public String get() {
 *     return USER_LOGIN;
 * }}</pre>
 * Sample messages.properties:
 * <pre>{@code application.title=My App
 * nav.login=Login}</pre>
 * Result title: <em>My App - Login</em>
 *
 * <pre>{@code @Title(title="nav.login", append=false)}</pre>
 * Result title: "Login"
 * <hr>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Title {

    @AliasFor("title")
    String value() default "";

    @AliasFor("value")
    String title() default "";

    boolean append() default true;
}
