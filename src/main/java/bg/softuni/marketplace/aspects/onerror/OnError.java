package bg.softuni.marketplace.aspects.onerror;

import bg.softuni.marketplace.web.alert.Alert;
import bg.softuni.marketplace.web.alert.AlertContainer;
import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.Errors;

import java.lang.annotation.*;

/**
 * See {@link OnErrorViewChangerAspect} for usage.
 *
 * @see #view()
 * @see #action()
 * @see #catchException()
 * @see #exceptionType()
 * @see #exceptionTypeIgnore()
 * @see #message()
 * @see #args()
 * @see #alert()
 * @see #ignoreMissingErrors()
 * @see Action
 * @see ErrorToAlert
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnError {

    @AliasFor("view")
    String value() default "";

    /**
     * View name to be used in case of errors on method invocation.
     * <p>
     * <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">
     * Spring Expression Language (SpEL)</a> could be used with this format:
     * <p>
     * view = "#{SpEL expression}"
     *
     * @see OnErrorViewChangerAspect
     */
    @AliasFor("value")
    String view() default "";

    /**
     * Defines {@link Action Action} type:
     * {@link Action#VIEW VIEW},
     * {@link Action#REDIRECT REDIRECT} or
     * {@link Action#FORWARD FORWARD}
     *
     * @see Action Action
     */
    Action action() default Action.VIEW;

    /**
     * Catch {@link #exceptionType} or its subclass exceptions on method invocation,
     * add {@link #message} to {@link Errors} argument and change view to {@link #view}.
     *
     * @see OnErrorViewChangerAspect
     */
    boolean catchException() default false;

    /**
     * Exception type and its subclasses to catch on method invocation.
     *
     * @see OnErrorViewChangerAspect
     * @see #catchException()
     */
    Class<? extends Throwable> exceptionType() default Throwable.class;

    /**
     * Exception type and its subclasses to NOT catch on method invocation.
     * This setting is with higher precedence than {@link #exceptionType()}.
     *
     * @see OnErrorViewChangerAspect
     * @see #catchException()
     */
    Class<? extends Throwable> exceptionTypeIgnore() default OnErrorViewChangerException.class;

    /**
     * Message to add as error to {@link Errors} argument when exception is cough on method invocation.
     *
     * @see OnErrorViewChangerAspect
     * @see #catchException()
     */
    String message() default "on-error.exception.default-message";

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
     * Add errors as {@link Alert} of type {@link Alert.Type#ERROR ERROR} to the {@link AlertContainer}:
     * <ul>
     * <li>{@link ErrorToAlert#NONE NONE} - no errors</li>
     * <li>{@link ErrorToAlert#GLOBAL GLOBAL} - add Global errors only (default)</li>
     * <li>{@link ErrorToAlert#FIELD FIELD} - add Field errors only</li>
     * <li>{@link ErrorToAlert#ALL ALL} - add both Global and Field errors</li>
     * </ul>
     *
     * @see ErrorToAlert
     */
    ErrorToAlert alert() default ErrorToAlert.GLOBAL;

    /**
     * Do not throw {@link OnErrorViewChangerException} if no param of type
     * {@link Errors} is present at annotated method.
     *
     * @see OnErrorViewChangerAspect
     */
    boolean ignoreMissingErrors() default false;

    /**
     * Defines the view type: {@link #VIEW},
     * {@link #REDIRECT} or
     * {@link #FORWARD}
     *
     * @see OnError#action()
     */
    enum Action {VIEW, REDIRECT, FORWARD}

    /**
     * Add errors as {@link Alert} of type {@link Alert.Type#ERROR ERROR} to the {@link AlertContainer}:
     * <ul>
     * <li>{@link #NONE} - no errors</li>
     * <li>{@link #GLOBAL} - add Global errors only (default)</li>
     * <li>{@link #FIELD} - add Field errors only</li>
     * <li>{@link #ALL} - add both Global and Field errors</li>
     * </ul>
     */
    enum ErrorToAlert {NONE, GLOBAL, FIELD, ALL}
}
