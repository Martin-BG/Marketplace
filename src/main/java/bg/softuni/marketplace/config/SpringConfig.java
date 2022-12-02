package bg.softuni.marketplace.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableCaching
@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableMethodSecurity(securedEnabled = true)
public class SpringConfig {
}
