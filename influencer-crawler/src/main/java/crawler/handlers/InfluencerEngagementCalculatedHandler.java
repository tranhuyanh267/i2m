package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerEngagementCalculatedEvent;
import crawler.entities.Influencer;
import crawler.repositories.InfluencerRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class InfluencerEngagementCalculatedHandler {

    private InfluencerRepository influencerRepository;

    @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_SAVE_ENGAGEMENT_QUEUE)
    public void handler(InfluencerEngagementCalculatedEvent event) {
        Optional<Influencer> influencerOpt = influencerRepository.findById(event.getInfluencerId());
        if (influencerOpt.isPresent()) {
            try {
                Influencer influencer = influencerOpt.get();
                influencer.setEngagement(event.getEngagement());
                influencerRepository.save(influencer);
            } catch(Exception ex) {

            }
        }
    }
}
