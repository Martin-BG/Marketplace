package bg.softuni.marketplace.aspects;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Execute validation on arguments followed by {@link Error}
 * on methods annotated with {@link Validate}.<br>
 * Optionally can catch specified exception type (and its sub-types) thrown by
 * method invocation and add message to provided {@link Error} object.<br>
 * In case of exception it returns {@link Optional#empty} if this is the
 * return type of the method or null in all other cases.<br><br>
 * Throws {@link IllegalArgumentException} if annotated method has no argument followed by {@link Error}<br>
 * <ul>Example 1:
 * <li>Just validate bindingModel</li>
 * </ul>
 * <pre>{@code
 * @Validate
 * public void testMe(@NotNull BindingModel bindingModel, @NotNull Errors errors) {
 *     if (!errors.hasErrors()) {
 *         // bindingModel is valid
 *     }
 * }}</pre>
 *
 * <ul>Example 2:
 * <li>Validate bindingModel</li>
 * <li>In case of any Runtime Exceptions add "message.from.message.properties"
 * to global errors and return {@link Optional#empty}</li></ul>
 * <pre>{@code
 * @Validate(catchException = true, exceptionType = RuntimeException.class, message = "message.from.message.properties")
 * public Optional<Boolean> registerUser(UserRegisterBindingModel bindingModel, Errors errors) {
 *     if (!errors.hasErrors()) {
 *         // bindingModel is valid
 *     }
 * }}</pre>
 * <hr>
 *
 * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop">
 * Aspect Oriented Programming with Spring</a>
 * @see <a href="https://stackoverflow.com/questions/50532039/methodvalidationinterceptor-and-validated-modelattribute">
 * Solution on similar problem @StackOverflow</a>
 * @see <a href="https://www.baeldung.com/spring-aop-annotation">
 * Implementing a Custom Spring AOP Annotation</a>
 * @see <a href="https://medium.com/@wkrzywiec/moving-into-next-level-in-user-log-events-with-spring-aop-3b4435892f16">
 * Moving into next level in user log events with Spring AOP</a>
 */

@Log
@Aspect
@Order //defaults to Ordered.LOWEST_PRECEDENCE
@Component
public class ValidateAspect {

    private final Validator validator;

    public ValidateAspect(Validator validator) {
        this.validator = validator;
    }

    @Around("@annotation(Validate)")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        List<MethodParameter> methodParameters = getMethodParameters(methodSignature);

        Errors errors = null;
        boolean isValidated = false;

        for (int i = 0; i < methodParameters.size() - 1; i++) {
            MethodParameter parameter = methodParameters.get(i);

            MethodParameter nextParameter = methodParameters.get(i + 1);
            if (!Errors.class.isAssignableFrom(nextParameter.getParameterType())) {
                // the Errors argument has to be right after the argument to form a pair
                continue;
            }

            isValidated = true;
            Object target = pjp.getArgs()[methodParameters.indexOf(parameter)];
            errors = (Errors) pjp.getArgs()[methodParameters.indexOf(nextParameter)];
            validator.validate(target, errors);
        }

        if (!isValidated) {
            throw new IllegalArgumentException(pjp.getSignature() + " is not applicable for validation");
        }

        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            Validate annotation = methodSignature.getMethod().getAnnotation(Validate.class);

            if (annotation.catchException() && errors != null
                    && annotation.exceptionType().isAssignableFrom(throwable.getClass())) {

                errors.reject(annotation.message());

                log.log(Level.WARNING, "ValidateAspect cough an Exception", throwable);

                if (methodSignature.getReturnType().equals(Optional.class)) {
                    return Optional.empty();
                }

                return null;
            }

            throw throwable;
        }
    }

    private static List<MethodParameter> getMethodParameters(MethodSignature methodSignature) {
        return IntStream
                .range(0, methodSignature.getParameterNames().length)
                .mapToObj(i -> new MethodParameter(methodSignature.getMethod(), i))
                .collect(Collectors.toList());
    }
}
