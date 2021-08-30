package bg.softuni.marketplace.web.interceptors;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Optional;

import static bg.softuni.marketplace.web.common.ViewActionPrefix.FORWARD;
import static bg.softuni.marketplace.web.common.ViewActionPrefix.REDIRECT;

final class Helper {

    private Helper() {
    }

    static <A extends Annotation> Optional<A> getMethodOrTypeAnnotation(Object handler, Class<A> annotationClass) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return Optional
                    .ofNullable(AnnotationUtils.getAnnotation(handlerMethod.getMethod(), annotationClass))
                    .or(() -> Optional.ofNullable(
                            AnnotationUtils.getAnnotation(handlerMethod.getBeanType(), annotationClass)));
        }
        return Optional.empty();
    }

    static boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith(REDIRECT) || viewName.startsWith(FORWARD);
    }
}
