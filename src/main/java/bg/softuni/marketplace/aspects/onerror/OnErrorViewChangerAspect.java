package bg.softuni.marketplace.aspects.onerror;

import bg.softuni.marketplace.aspects.Helper;
import bg.softuni.marketplace.web.alert.Alert;
import bg.softuni.marketplace.web.alert.AlertContainer;
import bg.softuni.marketplace.web.common.MessageHelper;
import bg.softuni.marketplace.web.common.ViewActionPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Wrap calls to {@link OnError} annotated Controller
 * methods and change returned view to {@link OnError#view} on errors.
 * <p>
 * Catch specified by {@link OnError#exceptionType} exception type (and sub-types)
 * thrown on method invocation when {@link OnError#catchException} is set.
 * <p>
 * Non-empty message set in {@link OnError#message message} with {@link OnError#args args}
 * is added as {@link Alert.Type#ERROR ERROR} to the {@link AlertContainer} on exception.
 * <p>
 * Add all errors as {@link Alert} of type {@link Alert.Type#ERROR ERROR} to the {@link AlertContainer}
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
 * and {@link OnError#ignoreMissingErrors} is {@code false}
 * <hr>
 * In the following example in case of any exception or validation errors returned value will be set to "/register".
 * "user" data is preserved and returned to the view.
 * <pre>
 * {@code @}PostMapping
 * {@code @}OnError(view = "/register", catchException = true)
 *  public String post(@Valid @ModelAttribute("user") User user,
 *                      Errors errors) {
 *      //... any exception thrown here will be handled automatically
 *      return "redirect:/login";
 * }</pre>
 * <hr>
 *
 * @see OnError
 */

@Log
@RequiredArgsConstructor
@Aspect
@Order
@Component
public class OnErrorViewChangerAspect {

    private static final Pattern SPEL_PATTERN = Pattern.compile("^#\\{(?<expression>.+)}$"); // "#{SpEL expression}"

    private final MessageHelper messageHelper;
    private final AlertContainer alertContainer;

    @Around("@annotation(OnError)")
    public Object onError(ProceedingJoinPoint pjp) throws Throwable {
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

        if (exceptionCough && !annotation.message().isEmpty()) {
            Object[] arguments = Helper.getArguments(
                    pjp.getArgs(),
                    methodSignature.getParameterNames(),
                    annotation.args());

            String message = messageHelper.getLocalizedMessage(annotation.message(), arguments);

            alertContainer.error(message);
        }

        List<Errors> errorsList = getNonNullErrorsParameters(pjp);

        if (errorsList.isEmpty() && !annotation.ignoreMissingErrors()) {
            throw new OnErrorViewChangerException(pjp.getSignature()
                    + " no non-null argument of type Errors.class found");
        }

        errorsList.removeIf(errors -> !errors.hasErrors());

        if (exceptionCough || !errorsList.isEmpty()) {
            String url = annotation.view();
            Matcher matcher = SPEL_PATTERN.matcher(url);
            if (matcher.matches()) {
                url = Helper.getValueFromExpression(
                        pjp.getArgs(), methodSignature.getParameterNames(), matcher.group("expression"), String.class);
            }
            result = buildView(annotation.action(), url);

            addErrorsToAlerts(errorsList, annotation.alert());
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
                .map(Errors.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static String buildView(OnError.Action action, String url) {
        return switch (action) {
            case FORWARD -> ViewActionPrefix.FORWARD + url;
            case REDIRECT -> ViewActionPrefix.REDIRECT + url;
            default -> url;
        };
    }

    private void addErrorsToAlerts(List<? extends Errors> errorsList, OnError.ErrorToAlert errorToAlert) {
        if (errorToAlert == OnError.ErrorToAlert.GLOBAL || errorToAlert == OnError.ErrorToAlert.ALL) {
            errorsList
                    .forEach(errors -> {
                        List<ObjectError> objectErrors = errors.getGlobalErrors();
                        objectErrors
                                .forEach(objectError -> {
                                    String message = objectError
                                            .unwrap(ConstraintViolation.class)
                                            .getMessage();
                                    alertContainer.error(message);
                                });
                    });
        }

        if (errorToAlert == OnError.ErrorToAlert.FIELD || errorToAlert == OnError.ErrorToAlert.ALL) {
            errorsList
                    .forEach(errors -> {
                        List<FieldError> fieldErrors = errors.getFieldErrors();
                        fieldErrors
                                .forEach(fieldError -> {
                                    String message = fieldError
                                            .unwrap(ConstraintViolation.class)
                                            .getMessage();
                                    alertContainer.error("* " + message);
                                });
                    });
        }
    }
}
