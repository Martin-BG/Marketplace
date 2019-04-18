package bg.softuni.marketplace.domain.validation.annotations.custom;

import org.springframework.core.annotation.AliasFor;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Values of all listed fields in the annotated class should not be equal.
 * <p>
 * Fields could be of any type that implements equals().
 * {@code null} values are also included in validation.
 *
 * @see EqualFields
 * @see EqualFieldsValidator
 */

@EqualFields(inverse = true)
@Repeatable(NotEqualFields.List.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface NotEqualFields {

    @OverridesAttribute(constraint = EqualFields.class, name = "message")
    String message() default "{not-equal-fields.default.match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AliasFor("fields")
    @OverridesAttribute(constraint = EqualFields.class, name = "value")
    String[] value() default {};

    @AliasFor("value")
    @OverridesAttribute(constraint = EqualFields.class, name = "fields")
    String[] fields() default {};

    /**
     * Defines several {@link NotEqualFields} annotations on the same element.
     *
     * @see NotEqualFields
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotEqualFields[] value();
    }
}
