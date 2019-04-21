package bg.softuni.marketplace.aspects.onerror;

import bg.softuni.marketplace.web.common.ViewActionPrefix;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Wrap calls to {@link OnError} annotated Controller
 * methods and change returned view to {@link OnError#view} on errors.
 * <br>
 * Catch specified by {@link OnError#exceptionType} exception type (and sub-types)
 * thrown on method invocation when {@link OnError#catchException} is set.
 * A message set in {@link OnError#message}
 * is added to all non-null arguments of type {@link Errors} on exception.
 * <p>
 * <ul>Annotated method is required to have:
 * <li>{@link String} return type</li>
 * <li>At lest one non-null parameter of type {@link Errors}</li>
 * </ul>
 * <p>
 * <ul>Returned value is changed to the one set in {@link OnError#view}
 * annotation in the following scenarios:
 * <li>Exception of type {@link OnError#exceptionType} if {@link OnError#catchException} is set</li>
 * <li>Errors in any of the non-null {@link Errors} arguments</li>
 * </ul>
 * <p>
 * Throws {@link OnErrorViewChangerException} for invalid method signature<br>
 * Throws {@link OnErrorViewChangerException} if annotated method has no non-null {@link Errors} arguments
 * <hr>
 * In the following example in case of any exception or validation errors returned value will be set to "/register".
 * "user" data is preserved and returned back to the view.
 * <pre>
 * {@code @}PostMapping
 * {@code @}OnError(view = "/register", catchException = true)
 *  public String post(@Valid @ModelAttribute("user") User user,
 *                      Errors errors) {
 *      //... any exception thrown here will be handled automatically
 *      return "redirect:/login";
 * }}</pre>
 * <hr>
 *
 * @see OnError
 */

@Log
@Aspect
@Order
@Component
public class OnErrorViewChangerAspect {

    @Around("@annotation(OnError)")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        if (!String.class.isAssignableFrom(methodSignature.getReturnType())) {
            throw new OnErrorViewChangerException(pjp.getSignature() + " return type is not String");
        }

        OnError annotation = Objects.requireNonNull( // cannot be null, added to please static code analysis
                AnnotationUtils.getAnnotation(methodSignature.getMethod(), OnError.class));

        String result = null;
        boolean exceptionCough = false;

        try {
            result = (String) pjp.proceed();
        } catch (Throwable throwable) {
            if (shouldCatch(annotation, throwable)) {
                exceptionCough = true;
                log.log(Level.WARNING, "@OnError Aspect cough an Exception", throwable);
            } else {
                throw throwable;
            }
        }

        List<Errors> errorsList = getNonNullErrorsParameters(pjp);

        if (errorsList.isEmpty()) {
            throw new OnErrorViewChangerException(pjp.getSignature()
                    + " no non-null argument of type Error.class found");
        }

        if (exceptionCough && !annotation.message().isEmpty()) {
            errorsList.forEach(error -> error.reject(annotation.message()));
        }

        errorsList.removeIf(errors -> !errors.hasErrors());

        if (exceptionCough || !errorsList.isEmpty()) {
            result = buildView(annotation);
        }

        return result;
    }

    private static boolean shouldCatch(OnError annotation, Throwable throwable) {
        return annotation.catchException()
                && annotation.exceptionType().isAssignableFrom(throwable.getClass())
                && !annotation.exceptionTypeIgnore().isAssignableFrom(throwable.getClass());
    }

    private static List<Errors> getNonNullErrorsParameters(ProceedingJoinPoint pjp) {
        return Arrays
                .stream(pjp.getArgs())
                .filter(Objects::nonNull)
                .filter(object -> Errors.class.isAssignableFrom(object.getClass()))
                .map(object -> (Errors) object)
                .collect(Collectors.toList());
    }

    private static String buildView(OnError annotation) {
        String url;
        switch (annotation.action()) {
        case FORWARD:
            url = ViewActionPrefix.FORWARD + annotation.view();
            break;
        case REDIRECT:
            url = ViewActionPrefix.REDIRECT + annotation.view();
            break;
        default:
            url = annotation.view();
            break;
        }
        return url;
    }
}
