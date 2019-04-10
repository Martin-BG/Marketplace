package bg.softuni.marketplace;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.logging.Level;

@Log
@SpringBootApplication
public class MarketplaceApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Value("${app.server.timezone}")
    private String systemTimeZone;

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    /**
     * Set system {@link TimeZone} to match setting used for database connection
     */
    @PostConstruct
    void systemConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone(systemTimeZone));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MarketplaceApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        log.log(Level.INFO, "Application deployed and ready to use");
    }
}
