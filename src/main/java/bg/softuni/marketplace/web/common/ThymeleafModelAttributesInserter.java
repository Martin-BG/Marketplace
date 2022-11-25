package bg.softuni.marketplace.web.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Add common attributes for use by Thymeleaf 3.1+ as starting from this version
 * "expression utility objects #request, #response, #session and #servletContext
 * are no longer available from expressions in templates."
 *
 * @see <a href="https://www.thymeleaf.org/doc/articles/thymeleaf31whatsnew.html">Thymeleaf 3.1: What’s new and how to migrate</a>
 */
@ControllerAdvice
public class ThymeleafModelAttributesInserter {

    @ModelAttribute("servletPath")
    public String servletPath(final HttpServletRequest request) {
        return request.getServletPath();
    }
}
