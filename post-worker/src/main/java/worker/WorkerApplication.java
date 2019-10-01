package worker;

import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;


@Log4j
@SpringBootApplication
public class WorkerApplication {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Instagram4j instagram4j;


    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

    @RabbitListener(queues = "userIdQueue")
    public void handleUserId(String userId) {
        log.info("Handling " + userId);
        String maxId = null;
        while (true) {
            System.out.println("handling " + maxId);
            InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(userId), maxId, 0, 0);
            try {
                InstagramFeedResult result = instagram4j.sendRequest(request);
                this.mongoTemplate.insert(result.getItems(), "posts");
                maxId = result.getNext_max_id();
                if (maxId == null) {
                    break;
                }
            } catch (IOException e) {
                log.error("Error while handling " + userId);
            }
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        System.out.println(event.getApplicationContext().getId());
    }

}
