package bg.softuni.marketplace.aspects.onsuccess;

import bg.softuni.marketplace.aspects.Helper;
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
    public void onSuccess(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        OnSuccess annotation = Objects.requireNonNull( // cannot be null, added to please static code analysis
                AnnotationUtils.getAnnotation(methodSignature.getMethod(), OnSuccess.class));

        if (!annotation.message().isEmpty() && hasErrors(joinPoint.getArgs())) {
            Object[] arguments = Helper.getArguments(
                    joinPoint.getArgs(),
                    methodSignature.getParameterNames(),
                    annotation.args());

            String message = messageHelper.getLocalizedMessage(annotation.message(), arguments);

            alertContainer.add(annotation.type(), message);
        }
    }

    private static boolean hasErrors(Object[] args) {
        return Arrays
                .stream(args)
                .filter(Objects::nonNull)
                .filter(object -> Errors.class.isAssignableFrom(object.getClass()))
                .map(object -> (Errors) object)
                .filter(Errors::hasErrors)
                .findAny()
                .isEmpty();
    }
}
