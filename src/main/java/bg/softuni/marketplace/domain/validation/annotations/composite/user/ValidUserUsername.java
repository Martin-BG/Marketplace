package bg.softuni.marketplace.domain.validation.annotations.composite.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_USERNAME_BLANK;
import static bg.softuni.marketplace.domain.validation.annotations.composite.MessageCodes.CODE_USER_USERNAME_LENGTH;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Cannot be blank, length {@value MIN_LENGTH}-{@value MAX_LENGTH}
 */

@NotBlank(message = "{" + CODE_USER_USERNAME_BLANK + "}")
@Size(message = "{" + CODE_USER_USERNAME_LENGTH + "}",
        min = ValidUserUsername.MIN_LENGTH, max = ValidUserUsername.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUserUsername {

    int MIN_LENGTH = 1;
    int MAX_LENGTH = 32;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
