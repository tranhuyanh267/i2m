package management.listeners;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostListener {
    @RabbitListener(queues = "post")
    public void handler() {

    }
}
