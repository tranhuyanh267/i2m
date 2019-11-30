package crawler;

import crawler.documents.InstagramUsername;
import crawler.messages.UsernameMessage;
import crawler.repos.InstagramUsernameRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class InstagramCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstagramCrawlerApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(InstagramUsernameRepository instagramUsernameRepository, RabbitTemplate rabbitTemplate) {
        return args -> {
            List<InstagramUsername> instagramUsernames = instagramUsernameRepository.findAll();
            for (InstagramUsername username : instagramUsernames) {
                UsernameMessage usernameMessage = new UsernameMessage();
                usernameMessage.setUsername(username.getUsername());
               // rabbitTemplate.convertAndSend("username-queue", usernameMessage);
            }
        };
    }

}
