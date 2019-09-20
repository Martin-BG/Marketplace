package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_MODIFY_INVALID_ID;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_USER_ID_MODIFY;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that {@link User User} with specified {@link User#getId() id} exist in database
 * and that the user is not with {@link Authority#ROOT ROOT} {@link Authority Authority}.
 */

@SpELAssert(message = "{" + CODE_USER_MODIFY_INVALID_ID + "}", value = EXPR_USER_ID_MODIFY)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ModifyUserById {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
