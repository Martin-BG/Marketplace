package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_EMAIL_INVALID;
import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_EMAIL_LENGTH;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Should be properly formatted email, length {@value MIN_LENGTH}-{@value MAX_LENGTH}
 */

@Email(message = "{" + CODE_USER_EMAIL_INVALID + "}")
@Size(message = "{" + CODE_USER_EMAIL_LENGTH + "}", min = ValidUserEmail.MIN_LENGTH, max = ValidUserEmail.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUserEmail {

    int MIN_LENGTH = 1;
    int MAX_LENGTH = 64;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
