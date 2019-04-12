package bg.softuni.marketplace.aspects;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * See {@link ValidateAspect} for details.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    @AliasFor("catchException")
    boolean value() default false;

    @AliasFor("value")
    boolean catchException() default false;

    Class<?> exceptionType() default Throwable.class;

    String message() default "validate.exception.default-message";
}
