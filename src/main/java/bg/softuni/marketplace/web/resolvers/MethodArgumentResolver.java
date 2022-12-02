package bg.softuni.marketplace.web.resolvers;

import bg.softuni.marketplace.web.resolvers.parameters.AbstractParameter;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Method argument resolver supporting subtypes of {@link AbstractParameter}.
 * <hr>
 * Resolver should be registered by adding to
 * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer WebMvcConfigurer}'s
 * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addArgumentResolvers addArgumentResolvers} list:
 * <br>
 * <br>
 * <pre>
 * &#64;RequiredArgsConstructor //Lombok
 * &#64;Configuration
 * public class WebConfig implements WebMvcConfigurer {
 *
 *     private final HandlerMethodArgumentResolver methodArgumentResolver;
 *
 *     &#64;Override
 *     public void addArgumentResolvers(List&#60;HandlerMethodArgumentResolver&#62; argumentResolvers) {
 *         argumentResolvers.add(methodArgumentResolver);
 *     }
 * }</pre>
 */
@Component
public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter
                .getParameterType()
                .isAssignableFrom(AbstractParameter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return parameter
                .getParameterType()
                .getDeclaredConstructor()
                .newInstance();
    }
}
