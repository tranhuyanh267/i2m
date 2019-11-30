package crawler;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class CommentCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommentCrawlerApplication.class, args);
    }

    @Bean
    public Instagram4j instagram4j() {
        Instagram4j instagram4j = Instagram4j.builder().username("hoangtunguyen43").password("vankute").build();
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

    @RabbitListener(queues = "comments")
    public void handle() {

    }
}
