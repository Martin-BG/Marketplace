package bg.softuni.marketplace.web.resolvers.parameters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract parameter class for use by
 * {@link bg.softuni.marketplace.web.resolvers.MethodArgumentResolver MethodArgumentResolver}.
 * <br>
 * Arguments of classes extending this can be injected
 * to {@link org.springframework.stereotype.Controller Controller} methods.
 *
 * <hr>
 * <pre>
 * public class StringParameter extends AbstractParameter&#60;String&#62; {
 * }
 *
 * &#64;Controller
 * public class TestController {
 *
 *     &#64;GetMapping("/")
 *     public String test(StringParameter stringParameter) {
 *         stringParameter.setValue("Test");
 *
 *         //...
 *     }
 * }
 * </pre>
 *
 * @param <T> the type parameter
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractParameter<T> {

    private T value;

    @Override
    public String toString() {
        return value.toString();
    }
}
