package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_EMAIL_USED;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_PROFILE_EMAIL_UNIQUE;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that annotated {@link String} value is not already used as
 * {@link Profile#getEmail() email} in another {@link User}'s {@link Profile}.
 */

@SpELAssert(message = "{" + CODE_USER_EMAIL_USED + "}", value = EXPR_PROFILE_EMAIL_UNIQUE)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface UniqueUserEmail {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
