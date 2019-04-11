package bg.softuni.marketplace.config;

import bg.softuni.marketplace.web.interceptors.ThymeleafLayoutInterceptor;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.TimeZone;

@Configuration
@EnableCaching
public class ApplicationConfig {

    @Value("${app.server.timezone}")
    private String systemTimeZone;

    /**
     * Set system {@link TimeZone} to match setting used for database connection
     */
    @PostConstruct
    void systemConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone(systemTimeZone));
    }

    /**
     * Configure ModelMapper to use field instead of property access for mapping between classes
     * and instances thus promoting better encapsulation and immutability.
     *
     * @return ModelMapper bean
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }

    /**
     * Configuration of runtime method arguments validation and return values validation.
     * {@link org.springframework.validation.annotation.Validated} annotation should be present on the class for validation to work.
     * <p>
     * Could be useful in repository methods to prevent request with invalid parameters
     * (ex. empty or not properly username in findUserByUsername)
     * <p><a href="https://www.baeldung.com/javax-validation-method-constraints">More information</a></p>
     * <hr>
     * <pre>{@code @Validated
     * @Repository
     * public interface UserRepository extends JpaRepository<User, UUID> {
     *
     *     Optional<User> findUserByUsername(@ValidUserUsername String username);
     * }</pre>
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * Configure Validator to use validation messages from custom file
     */
    @Bean
    public Validator validator(@Value("${app.messages.validation}") final String messagesBundle) {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(
                        new ResourceBundleMessageInterpolator(
                                new PlatformResourceBundleLocator(messagesBundle)
                        )
                )
                .buildValidatorFactory()
                .getValidator();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName(WebSecurityConfig.CSRF_ATTRIBUTE_NAME);
        return repository;
    }

    @Bean("thymeleafLayoutInterceptor")
    public HandlerInterceptor thymeleafLayoutInterceptor() {
        return ThymeleafLayoutInterceptor
                .builder()
                .withDefaultLayout("/layouts/default")
                .withViewAttribute("view")
                .withViewPrefix("/views/")
                .build();
    }
}
