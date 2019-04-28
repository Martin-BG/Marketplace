package bg.softuni.marketplace.aspects.onsuccess;

import bg.softuni.marketplace.web.alert.AlertContainer;
import bg.softuni.marketplace.web.common.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Objects;

/**
 * Add alert on successful execution of {@link OnSuccess} annotated Controller
 * methods. Successful execution means no exception is thrown and no errors
 * are present in any argument(s) of type {@link Errors}.
 * <p>
 * Message set in {@link OnSuccess#message} of type {@link OnSuccess#type}
 * is added to current {@link AlertContainer}.
 *
 * @see OnSuccess
 */

@Log
@RequiredArgsConstructor
@Aspect
@Order
@Component
public class OnSuccessAspect {

    private final MessageHelper messageHelper;
    private final AlertContainer alertContainer;

    @AfterReturning("@annotation(OnSuccess)")
    public void validate(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        OnSuccess annotation = Objects.requireNonNull( // cannot be null, added to please static code analysis
                AnnotationUtils.getAnnotation(methodSignature.getMethod(), OnSuccess.class));

        if (!annotation.message().isEmpty() && hasErrors(joinPoint)) {
            Object[] arguments = getArguments(joinPoint, methodSignature, annotation);

            String message = messageHelper.getLocalizedMessage(annotation.message(), arguments);

            alertContainer.add(annotation.type(), message);
        }
    }

    private static boolean hasErrors(JoinPoint joinPoint) {
        return Arrays
                .stream(joinPoint.getArgs())
                .filter(Objects::nonNull)
                .filter(object -> Errors.class.isAssignableFrom(object.getClass()))
                .map(object -> (Errors) object)
                .filter(Errors::hasErrors)
                .findAny()
                .isEmpty();
    }

    private static Object[] getArguments(JoinPoint joinPoint, MethodSignature methodSignature, OnSuccess annotation) {
        Object[] arguments = new Object[annotation.args().length];

        for (int i = 0; i < annotation.args().length; i++) {
            arguments[i] = getArgumentValue(
                    methodSignature.getParameterNames(),
                    joinPoint.getArgs(),
                    annotation.args()[i]);
        }

        return arguments;
    }

    /**
     * Inspired by Richa-b's
     * <a href="https://github.com/Richa-b/custom-annotation-with-dynamic-values-using-aop">
     * Getting Dynamic Values From method Parameters in Custom Annotations using Spring AOP</a>
     */
    private static Object getArgumentValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser
                .parseExpression(key)
                .getValue(context, Object.class);
    }
}
