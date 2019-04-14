package bg.softuni.marketplace.aspects.validate;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.Errors;

import java.lang.annotation.*;
import java.util.Optional;

/**
 * See {@link ValidateAspect} for details.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    @AliasFor("returnOnError")
    boolean value() default false;

    /**
     * Prevent method invocation in arguments errors
     *
     * @see ValidateAspect
     */
    @AliasFor("value")
    boolean returnOnError() default false;

    /**
     * Catch {@link #exceptionType} or its sub-class exceptions on method invocation,
     * add {@link #message} to {@link Errors} argument and return {@link Optional#empty()} or null.
     * @see ValidateAspect
     */
    boolean catchException() default false;

    /**
     * Exception type and its sub-classes to catch on method invocation.
     * @see ValidateAspect
     * @see #catchException()
     */
    Class<?> exceptionType() default Throwable.class;

    /**
     * Message to add as error to {@link Errors} argument when exception is cough on method invocation.
     * @see ValidateAspect
     * @see #catchException()
     */
    String message() default "validate.exception.default-message";
}
