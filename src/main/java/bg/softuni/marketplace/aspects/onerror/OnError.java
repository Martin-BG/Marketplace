package bg.softuni.marketplace.aspects.onerror;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.Errors;

import java.lang.annotation.*;

/**
 * See {@link OnErrorViewChangerAspect} for usage.
 *
 * @see #view()
 * @see #catchException()
 * @see #exceptionType()
 * @see #message()
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnError {

    @AliasFor("view")
    String value() default "";

    /**
     * View name to be used in case of errors on method invocation.
     *
     * @see OnErrorViewChangerAspect
     */
    @AliasFor("value")
    String view() default "";

    /**
     * Catch {@link #exceptionType} or its sub-class exceptions on method invocation,
     * add {@link #message} to {@link Errors} argument and change view to {@link #view}.
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
}
