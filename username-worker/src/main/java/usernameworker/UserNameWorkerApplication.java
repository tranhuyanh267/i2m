package usernameworker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class UserNameWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserNameWorkerApplication.class, args);
    }

    @RabbitListener(queues = "usernameQueue")
    @SendTo("userIdQueue")
    public String handleUsername(String username) {
        return username;
    }

}
