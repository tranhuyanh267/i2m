package calculation.core;

import common.events.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventBus {
    private RabbitTemplate rabbitTemplate;

    public void emit(Event event) {
        rabbitTemplate.convertAndSend("input-exchange", event.route(), event);
    }
}
