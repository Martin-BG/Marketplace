package bg.softuni.marketplace.web.interceptors;

import bg.softuni.marketplace.web.annotations.Title;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Modifies page title according to {@link Title} annotation.
 * <ul>Default values (can be changed during interceptor's instance build):
 * <li>Title code (as defined in *.properties text file): {@value #DEFAULT_TITLE_CODE}</li>
 * <li>View attribute name in Thymeleaf template: {@value #DEFAULT_TITLE_ATTRIBUTE_NAME}</li>
 * </ul>
 * <hr>
 * Sample html:
 * <pre>{@code <head>
 *     ...
 *     <title th:text="${title}"/>
 *     ...
 * </head>}</pre>
 */

public final class TitleInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_TITLE_CODE = "application.title";
    private static final String DEFAULT_TITLE_ATTRIBUTE_NAME = "title";

    private final MessageSource messageSource;
    private String titleCode;
    private String titleAttribute;

    private TitleInterceptor(MessageSource messageSource) {
        this.messageSource = messageSource;
        titleCode = DEFAULT_TITLE_CODE;
        titleAttribute = DEFAULT_TITLE_ATTRIBUTE_NAME;
    }

    public static TitleInterceptor.Builder builder(MessageSource messageSource) {
        return new TitleInterceptor.Builder(messageSource);
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           @Nullable ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod) {
            if (modelAndView == null || !modelAndView.hasView()) {
                return;
            }

            final String originalViewName = modelAndView.getViewName();
            if (originalViewName == null || Helper.isRedirectOrForward(originalViewName)) {
                return;
            }

            final Locale locale = LocaleContextHolder.getLocale();

            final String defaultTitle = messageSource.getMessage(titleCode, null, locale);

            final String title = Helper.getMethodOrTypeAnnotation(handler, Title.class)
                    .map(titleAnnotation -> {
                        final String newTitle = messageSource.getMessage(titleAnnotation.title(), null, locale);
                        return titleAnnotation.append()
                                ? (defaultTitle + " - " + newTitle)
                                : newTitle;
                    })
                    .orElse(defaultTitle);

            modelAndView.addObject(titleAttribute, title);
        }
    }

    public static final class Builder {
        private TitleInterceptor toBuild;

        private Builder(MessageSource messageSource) {
            toBuild = new TitleInterceptor(messageSource);
        }

        public Builder withTitleCode(String titleCode) {
            toBuild.titleCode = titleCode;
            return this;
        }

        public Builder withTitleAttribute(String titleAttribute) {
            toBuild.titleAttribute = titleAttribute;
            return this;
        }

        public TitleInterceptor build() {
            Assert.notNull(toBuild, "Builder cannot be reused");
            Assert.notNull(toBuild.messageSource, "MessageSource cannot be null");
            Assert.hasLength(toBuild.titleCode, "Title code cannot be empty or null");
            Assert.hasLength(toBuild.titleAttribute, "Title attribute name cannot be empty or null");

            TitleInterceptor built = toBuild;
            toBuild = null;
            return built;
        }
    }
}
