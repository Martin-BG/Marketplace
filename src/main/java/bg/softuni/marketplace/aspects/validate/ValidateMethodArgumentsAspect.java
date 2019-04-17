package bg.softuni.marketplace.aspects.validate;

import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Execute validation on arguments followed by {@link Errors}
 * on methods annotated with {@link Validate}.<br>
 * Optionally can validate method parameters and throw {@link ConstraintViolationException} on errors.<br>
 * Optionally can catch specified exception type (and its sub-types) thrown by
 * method invocation and add message to provided {@link Errors} object.<br>
 * Optionally can skip method invocation if validation fails.<br>
 * In case of exception or validation errors it returns {@link Optional#empty} if this is the
 * return type of the method or null in all other cases.<br><br>
 * Throws {@link ConstraintViolationException} if {@link Validate#validateParameters} is enabled and errors are found.<br>
 * Throws {@link ValidateMethodArgumentsException} if annotated method has no argument followed by {@link Errors}<br>
 * Throws {@link NullPointerException} if either the {@link Errors} or validated arguments are null<br>
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
 *
 * <ul>Example 3:
 * <li>Validate bindingModel</li>
 * <li>In case of any errors prevent method from invocation
 * and return {@link Optional#empty} instead</li></ul>
 * <pre>{@code
 * @Validate(true)
 * public Optional<Boolean> registerUser(UserRegisterBindingModel bindingModel, Errors errors) {
 *  // never gets here for incorrect bindingModel
 * }}</pre>
 * <hr>
 *
 * @see Validate#groups()
 * @see MethodValidationInterceptor#invoke(MethodInvocation)
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
@Order(Ordered.HIGHEST_PRECEDENCE) // HIGHEST_PRECEDENCE to ensure exception handling on method invocation
@Component
public class ValidateMethodArgumentsAspect {

    private final SmartValidator validator;
    private final ExecutableValidator executableValidator;

    @Autowired
    public ValidateMethodArgumentsAspect(SmartValidator validator, Validator javaxValidator) {
        this.validator = validator;
        executableValidator = javaxValidator.forExecutables();
    }

    @Around("@annotation(Validate)")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        Validate annotation = Objects.requireNonNull( // cannot be null, added to please static code analysis
                AnnotationUtils.getAnnotation(methodSignature.getMethod(), Validate.class));

        if (annotation.validateParameters()) {
            validateParameters(pjp, methodSignature, annotation);
        }

        List<MethodParameter> methodParameters = getMethodParameters(methodSignature);
        Object[] validationGroups = annotation.groups();

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
            Object target = Objects.requireNonNull(
                    pjp.getArgs()[methodParameters.indexOf(parameter)]);
            errors = Objects.requireNonNull(
                    (Errors) pjp.getArgs()[methodParameters.indexOf(nextParameter)]);
            validator.validate(target, errors, validationGroups);
        }

        if (!isValidated) {
            throw new ValidateMethodArgumentsException(pjp.getSignature() + " is not applicable for validation");
        }

        if (annotation.returnOnError() && errors.hasErrors()) {
            return getDefaultReturnObject(methodSignature);
        }

        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            if (annotation.catchException() && annotation.exceptionType().isAssignableFrom(throwable.getClass())) {
                log.log(Level.WARNING, "@Validate Aspect cough an Exception", throwable);

                errors.reject(annotation.message());

                return getDefaultReturnObject(methodSignature);
            }

            throw throwable;
        }
    }

    private void validateParameters(ProceedingJoinPoint pjp, MethodSignature methodSignature, Validate annotation) {
        Object object = pjp.getThis();
        Method method = methodSignature.getMethod();
        Object[] parameterValues = pjp.getArgs();
        Class<?>[] groups = annotation.groups();

        Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
                object, method, parameterValues, groups);

        if (!constraintViolations.isEmpty()) {
            String message = String.format(
                    "%d constraint violation(s) detected on [%s] method parameter(s) validation:%n\t* %s",
                    constraintViolations.size(),
                    pjp.getSignature(),
                    constraintViolationsToString(constraintViolations));
            throw new ConstraintViolationException(message, constraintViolations);
        }
    }

    private static String constraintViolationsToString(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(cv -> (cv == null) ? "null" : (cv.getPropertyPath() + ": " + cv.getMessage()))
                .collect(Collectors.joining("\n\t* "));
    }

    private static List<MethodParameter> getMethodParameters(MethodSignature methodSignature) {
        return IntStream
                .range(0, methodSignature.getParameterNames().length)
                .mapToObj(i -> new MethodParameter(methodSignature.getMethod(), i))
                .collect(Collectors.toList());
    }

    private static Object getDefaultReturnObject(MethodSignature methodSignature) {
        if (methodSignature.getReturnType().equals(Optional.class)) {
            return Optional.empty();
        }

        return null;
    }
}
