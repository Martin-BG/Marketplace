package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_PASSWORD_EMPTY;
import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_PASSWORD_LENGTH;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Cannot be empty, length {@value MIN_LENGTH}-{@value MAX_LENGTH}
 */

@NotEmpty(message = "{" + CODE_USER_PASSWORD_EMPTY + "}")
@Size(message = "{" + CODE_USER_PASSWORD_LENGTH + "}",
        min = ValidUserPassword.MIN_LENGTH, max = ValidUserPassword.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUserPassword {

    int MIN_LENGTH = 1;
    int MAX_LENGTH = 32;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
