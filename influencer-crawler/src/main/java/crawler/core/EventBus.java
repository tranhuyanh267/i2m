package crawler.core;

import common.events.Event;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventBus {
    private RabbitTemplate rabbitTemplate;

    public void emit(Event event) {
        rabbitTemplate.convertAndSend(event.route(), event);

    }
}
