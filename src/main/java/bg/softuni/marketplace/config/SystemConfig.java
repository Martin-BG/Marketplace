package bg.softuni.marketplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class SystemConfig {

    @Value("${app.server.timezone}")
    private String systemTimeZone;

    /**
     * Set system {@link TimeZone} to match setting used for database connection
     */
    @PostConstruct
    void systemConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone(systemTimeZone));
    }
}
