package calculation.core;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventBus {
    private RabbitTemplate rabbitTemplate;

    public void emit(String exchange, Object payload) {
        rabbitTemplate.convertAndSend(exchange, "*", payload);
    }
}
