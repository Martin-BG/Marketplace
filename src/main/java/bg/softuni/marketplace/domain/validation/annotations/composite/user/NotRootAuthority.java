package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_MODIFY_ROOT_AUTHORITY;
import static bg.softuni.marketplace.domain.validation.annotations.composite.SpelExpressions.EXPR_AUTHORITY_NOT_ROOT;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that annotated {@link Authority} is not of type {@link Authority#ROOT ROOT}.
 */

@SpELAssert(message = "{" + CODE_USER_MODIFY_ROOT_AUTHORITY + "}", value = EXPR_AUTHORITY_NOT_ROOT)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface NotRootAuthority {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
