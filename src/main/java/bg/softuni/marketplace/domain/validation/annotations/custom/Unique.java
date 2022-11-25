package bg.softuni.marketplace.domain.validation.annotations.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates annotated value against the return value of a {@link #method}.<br>
 * Annotated value is {@code true} if {@code null}
 * or if {@link #bean}.{@link #method} invocation returns 0.
 *
 * @see UniqueValidator
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Unique.List.class)
@Documented
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String message() default "{unique.default.not-unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Class of the bean to load from Spring context:
     * <p>
     * Ex: "UserRepository.class" for
     * <pre>
     * {@code @}Repository
     *  public interface UserRepository extends GenericRepository<{@code User, UUID>}{
     *      long countByUsername(String username);
     *      long countByEmail(String email);
     *  }
     * </pre>
     */
    Class<?> bean();

    /**
     * {@link #bean}'s method name that is invoked.<br>
     * The method is required to return {@code long} or {@link Long}
     * and to have exactly one argument of type {@link String}:
     * <pre>{@code
     *     long countByUsername(String username);
     *     long countByEmail(String email);
     * }</pre>
     *
     * @see UniqueValidator
     */
    String method();

    /**
     * Defines several {@link Unique} annotations on the same element.
     *
     * @see Unique
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Unique[] value();
    }
}
