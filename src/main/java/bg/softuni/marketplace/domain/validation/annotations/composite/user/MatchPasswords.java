package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.validation.annotations.custom.EqualFields;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_PASSWORDS_NOT_MATCH;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that values of 'password' and 'confirmPassword' fields match,
 * otherwise value in 'confirmPassword' is considered invalid.
 */

@EqualFields(message = "{" + CODE_USER_PASSWORDS_NOT_MATCH + "}",
        fields = {"password", "confirmPassword"},
        forField = "confirmPassword")
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface MatchPasswords {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
