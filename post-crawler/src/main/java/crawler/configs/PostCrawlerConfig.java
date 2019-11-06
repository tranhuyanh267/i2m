package crawler.configs;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class PostCrawlerConfig {
    @Bean
    public Instagram4j instagram4j() {
        //Instagram4j instagram4j = Instagram4j.builder().username("flower_road_123").password("vankute").build();
        Instagram4j instagram4j = Instagram4j.builder().username("cloud_vnn").password("vankute").build();
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
