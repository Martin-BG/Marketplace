package bg.softuni.marketplace.web.interceptors;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;

import static bg.softuni.marketplace.web.common.ViewActionPrefix.FORWARD;
import static bg.softuni.marketplace.web.common.ViewActionPrefix.REDIRECT;

final class Helper {

    private Helper() {
    }

    static <A extends Annotation> A getMethodOrTypeAnnotation(Object handler, Class<A> annotationClass) {
        if (handler instanceof HandlerMethod handlerMethod) {
            A annotation = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), annotationClass);
            if (annotation == null) {
                annotation = AnnotationUtils.getAnnotation(handlerMethod.getBeanType(), annotationClass);
            }
            return annotation;
        }
        return null;
    }

    static boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith(REDIRECT) || viewName.startsWith(FORWARD);
    }
}
