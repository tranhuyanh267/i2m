package crawler.configs;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class InfluencerCrawlerConfig {
    @Bean
    public Instagram4j instagram4j() {
        Instagram4j instagram4j = Instagram4j.builder().username("huyanh267").password("huyanhkute").build();
        instagram4j.setup();
        try {
            instagram4j.login();
        } catch (IOException ignored) {

        }
        return instagram4j;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
