package usernameworker;

import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Log4j
@Configuration
public class InstagramConfig {

    @Bean
    public Instagram4j instagram4j() {
        Instagram4j instagram4j = Instagram4j.builder().username("huyanh267").password("huyanhkute").build();
        instagram4j.setup();
        try {
            instagram4j.login();
        } catch (IOException e) {
            log.error("Error while create instagram client");
        }
        return instagram4j;
    }

}
