package bg.softuni.marketplace.aspects.validate;

import jakarta.validation.groups.Default;
import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.Errors;

import java.lang.annotation.*;
import java.util.Optional;

/**
 * See {@link ValidateMethodArgumentsAspect} for usage.
 *
 * @see #validateParameters()
 * @see #returnOnError()
 * @see #catchException()
 * @see #exceptionType()
 * @see #message()
 * @see #args()
 * @see #groups()
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    @AliasFor("returnOnError")
    boolean value() default false;

    /**
     * Validate method parameters first and throw {@link jakarta.validation.ConstraintViolationException} on errors.
     * <hr>
     * Exception will be thrown on null value for either user or errors in the following example:
     * <pre>
     *    {@code @}Validate(validateParameters = true)
     *     public void registerUser(@NotNull User user, @NotNull Errors errors) {
     *         ...
     *     }
     * </pre>
     *
     * @see ValidateMethodArgumentsAspect
     */
    boolean validateParameters() default true;

    /**
     * Prevent method invocation in case of arguments errors
     *
     * @see ValidateMethodArgumentsAspect
     */
    @AliasFor("value")
    boolean returnOnError() default false;

    /**
     * Catch {@link #exceptionType} or its subclass exceptions on method invocation,
     * add {@link #message} to {@link Errors} argument and return {@link Optional#empty()} or null.
     *
     * @see ValidateMethodArgumentsAspect
     */
    boolean catchException() default false;

    /**
     * Exception type and its subclasses to catch on method invocation.
     *
     * @see ValidateMethodArgumentsAspect
     * @see #catchException()
     */
    Class<? extends Throwable> exceptionType() default Throwable.class;

    /**
     * Message to add as error to {@link Errors} argument when exception is cough on method invocation.
     *
     * @see ValidateMethodArgumentsAspect
     * @see #catchException()
     */
    String message() default "validate.exception.default-message";

    /**
     * Message arguments - values defined using Spring Expression Language
     * <p>
     * args = {"#user.username", "#user.email"}
     *
     * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">
     * Spring Expression Language (SpEL) Documentation</a>
     */
    String[] args() default {};

    /**
     * Validation groups. Failed validation in one group prevents validation of groups after it.
     *
     * <pre>
     *  public interface GroupOne {}
     *  public interface GroupTwo {}
     *
     * {@code @}Validate(groups = {GroupOne.class, GroupTwo.class})
     *  public void testMe(User user, Error error) {}
     *
     *  // Alternative usage
     * {@code @}GroupSequence({GroupOne.class, GroupTwo.class})
     *  public interface MySequence {}
     *
     * {@code @}Validate(groups ={MySequence.class})
     *  public void testMe(User user, Error error) {}
     *
     *  public class User {
     *    {@code @}NotNull(groups = {GroupOne.class}) // cheap and fast validation
     *    {@code @}Unique(groups = {GroupTwo.class}) // expensive validation with repository call,
     *     // executed only if both username and email are not null
     *     private String username;
     *
     *    {@code @}NotNull(groups = {GroupOne.class}) // cheap and fast validation
     *     private String email;
     *     // ..
     * }
     * </pre>
     *
     * @see jakarta.validation.GroupSequence
     */
    Class<?>[] groups() default {Default.class};
}
