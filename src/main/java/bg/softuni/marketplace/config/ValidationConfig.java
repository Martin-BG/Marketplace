package bg.softuni.marketplace.config;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintValidator;

@Configuration
public class ValidationConfig {

    /**
     * Configuration of runtime method arguments validation and return values validation.
     * {@link org.springframework.validation.annotation.Validated} annotation should be present on the class for validation to work.
     * <p>
     * Could be useful in repository methods to prevent request with invalid parameters
     * (ex. empty or not properly username in findUserByUsername)
     * <p><a href="https://www.baeldung.com/javax-validation-method-constraints">More information</a></p>
     * <hr>
     * <pre>
     * {@code @}Validated
     * {@code @}Repository
     *  public interface UserRepository extends JpaRepository< User, UUID> {
     *
     *      Optional< User> findUserByUsername(@ValidUserUsername String username);
     *  }</pre>
     *
     * @see ValidationAutoConfiguration#methodValidationPostProcessor methodValidationPostProcessor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * Override default Spring validator configuration.
     *
     * @param messageSource              required for reading messages from *.properties
     * @param constraintValidatorFactory required for proper creation (with CDI) of custom {@link ConstraintValidator}
     * @see ValidationAutoConfiguration#defaultValidator defaultValidator
     */
    @Bean
    public LocalValidatorFactoryBean defaultValidator(MessageSource messageSource,
                                                      SpringConstraintValidatorFactory constraintValidatorFactory) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        bean.setConstraintValidatorFactory(constraintValidatorFactory);
        return bean;
    }

    /**
     * Required by {@link LocalValidatorFactoryBean} for creating of custom {@link ConstraintValidator}
     * (autowire all dependencies)
     *
     * @param defaultListableBeanFactory {@link DefaultListableBeanFactory}
     * @return {@link SpringConstraintValidatorFactory}
     */
    @Bean
    public SpringConstraintValidatorFactory springConstraintValidatorFactory(
            DefaultListableBeanFactory defaultListableBeanFactory) {
        return new SpringConstraintValidatorFactory(defaultListableBeanFactory);
    }
}
