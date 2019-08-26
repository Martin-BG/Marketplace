package bg.softuni.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@Configuration
public class SpELConfig {

    /**
     * Expose Spring Security as SpEL expressions for creating Spring Data queries
     *
     * @see SecurityEvaluationContextExtension
     * @see <a href="https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#data-configuration">Spring Documentation</a>
     */
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
