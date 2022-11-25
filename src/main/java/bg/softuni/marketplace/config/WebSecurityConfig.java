package bg.softuni.marketplace.config;

import bg.softuni.marketplace.domain.enums.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    public static final String CSRF_ATTRIBUTE_NAME = "_csrf";

    private static final int REMEMBER_ME_TOKEN_VALIDITY_SECONDS = (int) Duration.ofDays(30L).toSeconds();
    private static final String REMEMBER_ME_KEY = "remember-me-key";
    private static final String REMEMBER_ME_COOKIE = "remember-me";
    private static final String SESSION_COOKIE = "JSESSIONID";

    private final UserDetailsService userService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CsrfTokenRepository csrfTokenRepository;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .cors()
                .disable()
            .csrf()
                .csrfTokenRepository(csrfTokenRepository)
                .and()
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                .requestMatchers(WebConfig.URL_INDEX, WebConfig.URL_USER_LOGIN)
                    .permitAll()
                .requestMatchers(WebConfig.URL_USER_REGISTER)
                    .anonymous()
                .requestMatchers(WebConfig.URL_ADMIN_BASE + "/**")
                    .hasRole(Authority.ADMIN.name())
                .anyRequest()
                    .authenticated())
            .formLogin()
                .loginPage(WebConfig.URL_USER_LOGIN)
                .defaultSuccessUrl(WebConfig.URL_USER_HOME)
                .permitAll()
                .and()
            .rememberMe()
                .userDetailsService(userService)
                .tokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY_SECONDS)
                .key(REMEMBER_ME_KEY)
                .rememberMeCookieName(REMEMBER_ME_COOKIE)
                .and()
            .logout()
                .logoutUrl(WebConfig.URL_USER_LOGOUT)
                .deleteCookies(SESSION_COOKIE, REMEMBER_ME_COOKIE)
                .logoutSuccessUrl(WebConfig.URL_USER_LOGIN + "?logout")
                .and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .and()
            .sessionManagement()
                .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl(WebConfig.URL_USER_LOGIN + "?expired")
                    .and()
                .sessionFixation()
                    .changeSessionId()
                .invalidSessionUrl(WebConfig.URL_USER_LOGIN + "?invalid");
        // @formatter:on
        return http.build();
    }
}
