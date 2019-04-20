package bg.softuni.marketplace.aspects.onerror;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.Errors;

import java.lang.annotation.*;

/**
 * See {@link OnErrorViewChangerAspect} for usage.
 *
 * @see #path()
 * @see #catchException()
 * @see #exceptionType()
 * @see #message()
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnError {

    @AliasFor("path")
    String value() default "";

    /**
     * View name to be used in case of errors on method invocation.
     *
     * @see OnErrorViewChangerAspect
     */
    @AliasFor("value")
    String path() default "";

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
     * Catch {@link #exceptionType} or its sub-class exceptions on method invocation,
     * add {@link #message} to {@link Errors} argument and change path to {@link #path}.
     *
     * @see OnErrorViewChangerAspect
     */
    boolean catchException() default false;

    /**
     * Exception type and its sub-classes to catch on method invocation.
     *
     * @see OnErrorViewChangerAspect
     * @see #catchException()
     */
    Class<? extends Throwable> exceptionType() default Throwable.class;

    /**
     * Exception type and its sub-classes to NOT catch on method invocation.
     * This setting is with higher precedence than {@link #exceptionType()}
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
     * Defines the view type: {@link #VIEW},
     * {@link #REDIRECT} or
     * {@link #FORWARD}
     *
     * @see OnError#action()
     */
    enum Action {VIEW, REDIRECT, FORWARD}
}
