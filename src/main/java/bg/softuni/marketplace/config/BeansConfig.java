package bg.softuni.marketplace.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class BeansConfig {

    /**
     * Configure ModelMapper to use field instead of property access for mapping between classes
     * and instances thus promoting better encapsulation and immutability.
     * <p>
     * Expect a single "Illegal reflective access by org.modelmapper.internal.PropertyInfoImpl$FieldPropertyInfo..."
     * warning in logs on first usage because of a ModelMapper
     * <a href="https://github.com/modelmapper/modelmapper/issues/414#issuecomment-469730463">issue</a>
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
}
