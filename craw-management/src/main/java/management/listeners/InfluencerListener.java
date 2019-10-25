package management.listeners;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InfluencerListener {
    @RabbitListener(queues = "influencer")
    public void handler() {

    }
}
