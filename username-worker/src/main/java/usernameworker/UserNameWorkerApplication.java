package usernameworker;

import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import java.io.IOException;

@Log4j
@SpringBootApplication
public class UserNameWorkerApplication {

    @Autowired
    private Instagram4j instagram4j;

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(UserNameWorkerApplication.class, args);
    }

    @RabbitListener(queues = "usernameQueue")
    @SendTo("userIdQueue")
    public long handleUsername(String username) {
        try {
            log.info("doing handleUsername " + username);
            InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(username);
            InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
            mongoTemplate.save(result.getUser());
            return result.getUser().getPk();
        } catch (IOException e) {
            log.error("error handleUsername " + username);
            return 0;
        }
    }

}
