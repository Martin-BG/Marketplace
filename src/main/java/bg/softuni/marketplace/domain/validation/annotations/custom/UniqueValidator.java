package bg.softuni.marketplace.domain.validation.annotations.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Validation logic for {@link Unique} annotation.
 * <p>
 * Loads specified {@link Unique#bean} from Spring context and invokes {@link Unique#method} on it.<br>
 * {@link Unique#method} is required to return {@code long} or {@link Long} and to have exactly one
 * argument of type {@link String}:
 * <pre>{@code
 *     long countByUsername(String username);
 *     long countByEmail(String email);
 * }</pre>
 * <p>
 * Returns {@code true} when tested value is {@code null} or
 * {@link Unique#method} invocation result is {@code 0}.
 *
 * @see Unique
 */

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final ApplicationContext applicationContext;

    private Method method;
    private Object object;

    @Override
    public void initialize(Unique constraintAnnotation) {
        object = applicationContext.getBean(constraintAnnotation.bean());
        try {
            method = object.getClass().getMethod(constraintAnnotation.method(), String.class);
        } catch (NoSuchMethodException e) {
            throw new UniqueValidatorException("Failed to get method: " + constraintAnnotation.method(), e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            long result = (long) method.invoke(object, value);
            return result == 0L;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UniqueValidatorException("Method invocation failed", e);
        }
    }
}
