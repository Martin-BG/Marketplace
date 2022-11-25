package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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

@SpELAssert(message = "{" + CODE_USER_PASSWORDS_NOT_MATCH + "}",
        value = "#this.password ne null and #this.password.equals(#this.confirmPassword)",
        fields = "confirmPassword")
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface MatchPasswords {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
