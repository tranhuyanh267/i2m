package worker.configs;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

public class InstagramConfig {

    @Bean
    public Instagram4j instagram4j() throws IOException {
        Instagram4j instagram4j = Instagram4j.builder().username("huyanh267").password("huyanhkute").build();
        instagram4j.setup();
        instagram4j.login();
        return instagram4j;
    }
}
