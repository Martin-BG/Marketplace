package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_DELETE_INVALID_ID;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_USER_ID_DELETE;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that {@link User} with specified {@link User#getId() id}
 * exists in database and that this user is nether {@link User#isActive() active}
 * or with {@link Authority#ROOT ROOT} {@link Authority}.
 */

@SpELAssert(message = "{" + CODE_USER_DELETE_INVALID_ID + "}", value = EXPR_USER_ID_DELETE)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface DeleteUserById {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
