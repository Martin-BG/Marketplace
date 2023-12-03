package bg.softuni.marketplace.config;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import jakarta.validation.ConstraintValidator;

@Configuration
public class ValidationConfig {

    /**
     * Override default Spring validator configuration.
     *
     * @param messageSource              required for reading messages from *.properties
     * @param constraintValidatorFactory required for proper creation (with CDI) of custom {@link ConstraintValidator}
     * @see ValidationAutoConfiguration#defaultValidator defaultValidator
     */
    @Bean
    @Primary
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
