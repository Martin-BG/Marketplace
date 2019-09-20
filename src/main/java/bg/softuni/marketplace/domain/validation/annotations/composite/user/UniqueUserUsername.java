package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_USERNAME_USED;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_USER_USERNAME_UNIQUE;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that annotated {@link String} value is not already used
 * as {@link User#getUsername() username} by another {@link User}.
 */

@SpELAssert(message = "{" + CODE_USER_USERNAME_USED + "}", value = EXPR_USER_USERNAME_UNIQUE)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface UniqueUserUsername {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
