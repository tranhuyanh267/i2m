package worker;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramCheckUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class WorkerApplication implements ApplicationRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Instagram4j instagram4j = Instagram4j.builder().username("huyanh267").password("huyanhkute").build();
        instagram4j.setup();
        instagram4j.login();
        InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest("stephencurry30");
        InstagramSearchUsernameResult result = instagram4j.sendRequest(request);

        System.out.println(result);
    }

    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }

    @RabbitListener(queues = "myQueue")
    public void listen(String in) {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Message read from myQueue : " + in);
    }

    @RabbitListener(queues = "myQueue")
    public void listen1(String in) {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Message read from myQueue 1: " + in);
    }

}


