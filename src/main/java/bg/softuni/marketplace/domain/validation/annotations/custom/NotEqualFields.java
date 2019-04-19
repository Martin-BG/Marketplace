package bg.softuni.marketplace.domain.validation.annotations.custom;

import org.springframework.core.annotation.AliasFor;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(NotEqualFields.List.class)
@Documented
@Constraint(validatedBy = {})
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
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotEqualFields[] value();
    }
}
