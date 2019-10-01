package management;

import management.documents.Influencer;
import management.repos.InfluencerRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ManagementApplication implements ApplicationRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private InfluencerRepo influencerRepo;

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int count = 0;
        List<Influencer> influencers = influencerRepo.findAll();
        for (Influencer influencer : influencers) {
            if (count > 200) {
                rabbitTemplate.convertAndSend("usernameQueue", influencer.getUsername());
            }
            count++;
            if (count > 500) {
                break;
            }
        }

    }
}
