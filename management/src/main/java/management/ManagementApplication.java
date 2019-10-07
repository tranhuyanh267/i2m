package management;

import common.QueueName;
import management.documents.InfluencerUsername;
import management.repos.InfluencerUsernameRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import payloads.InstagramUserPayload;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class ManagementApplication implements ApplicationRunner {

    @Autowired
    private InfluencerUsernameRepo influencerUsernameRepo;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<InfluencerUsername> influencers = influencerUsernameRepo.findAll();
        List<InstagramUserPayload> instagrams = influencers.stream().map(influencer -> {
            InstagramUserPayload instagramUserPayload = new InstagramUserPayload();
            instagramUserPayload.setUsername(influencer.getUsername());
            instagramUserPayload.setCategories(Collections.singletonList(influencer.getCategory()));
            return instagramUserPayload;
        }).collect(Collectors.toList());

        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend(QueueName.USER_NAME_QUEUE, instagrams.get(i));
        }

    }
}
