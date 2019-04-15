package bg.softuni.marketplace.domain.validation.annotations.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Validates annotated value against the return value of a {@link #method}.<br>
 * Annotated value is {@code true} if {@code null}
 * or if {@link #bean}.{@link #method} invocation returns 0.
 *
 * @see UniqueValidator
 */

@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
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
}
