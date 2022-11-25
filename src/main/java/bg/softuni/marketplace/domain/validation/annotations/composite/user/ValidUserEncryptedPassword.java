package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Cannot be null, length is {@value MIN_LENGTH}-{@value MAX_LENGTH}
 */

@NotNull
@Size(min = ValidUserEncryptedPassword.MIN_LENGTH, max = ValidUserEncryptedPassword.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUserEncryptedPassword {

    int MIN_LENGTH = 60;
    int MAX_LENGTH = 60;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
