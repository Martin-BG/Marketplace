package bg.softuni.marketplace.domain.validation.annotations.custom;

import org.springframework.core.annotation.AliasFor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Values of all listed fields in the annotated class should be equal.
 * <p>
 * Fields could be of any type that implements equals().
 * {@code null} values are also included in validation.
 */

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(EqualFields.List.class)
@Documented
@Constraint(validatedBy = EqualFieldsValidator.class)
public @interface EqualFields {

    String message() default "{equal-fields.default.not-match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @AliasFor("fields")
    String[] value() default {};

    @AliasFor("value")
    String[] fields() default {};

    /**
     * Inverse validation result, i.e. checks for not equality
     */
    boolean inverse() default false;

    /**
     * Defines several {@link EqualFields} annotations on the same element.
     *
     * @see EqualFields
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EqualFields[] value();
    }
}
