package bg.softuni.marketplace.aspects.onsuccess;

import bg.softuni.marketplace.web.alert.Alert;
import bg.softuni.marketplace.web.alert.AlertContainer;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * See {@link OnSuccessAspect} for usage.
 *
 * @see #message()
 * @see #args()
 * @see #type()
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnSuccess {

    @AliasFor("message")
    String value() default "";

    /**
     * Message code to add as {@link Alert} to {@link AlertContainer} on success.
     *
     * @see OnSuccessAspect
     */
    @AliasFor("value")
    String message() default "";

    /**
     * Message arguments - values defined using Spring Expression Language
     * <p>
     * args = {"#user.username", "#user.email"}
     *
     * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">
     * Spring Expression Language (SpEL) Documentation</a>
     */
    String[] args() default {};

    /**
     * Message code to add as {@link Alert} to {@link AlertContainer} on success.
     *
     * @see Alert.Type
     */
    Alert.Type type() default Alert.Type.SUCCESS;
}
