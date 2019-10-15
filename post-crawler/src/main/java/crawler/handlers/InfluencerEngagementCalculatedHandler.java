package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import common.events.InfluencerEngagementCalculatedEvent;
import crawler.core.EventBus;
import lombok.AllArgsConstructor;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class InfluencerEngagementCalculatedHandler {

    private Instagram4j instagram4j;
    private EventBus eventBus;

    @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_CALCULATE_ENGAGEMENT_QUEUE)
    public void handler(InfluencerCreatedEvent event) {
        if (event.getFollowers() == 0) {
            return;
        }
        try {
            List<Float> postEngagements = new ArrayList<>();
            String maxId = "";
            do {
                InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(event.getInfluencerId()), maxId, 0, 0);
                InstagramFeedResult result = instagram4j.sendRequest(request);
                if (result.getItems() != null) {
                    List<InstagramFeedItem> items = result.getItems();
                    items.forEach(item -> {
                        float postEngagement = calculatePostEngagement(item, event.getFollowers());
                        postEngagements.add(postEngagement);
                    });
                }
                maxId = result.getNext_max_id();
            }
            while (maxId != null && postEngagements.size() < 50);
            float influencerEngagement = calculateInfluencerEngagement(postEngagements);
            eventBus.emit(new InfluencerEngagementCalculatedEvent(event.getInfluencerId(), influencerEngagement));
        } catch (Exception e) {

        }
    }

    float calculatePostEngagement(InstagramFeedItem item, int influencerFollowers) {
        return (item.getComment_count() + item.getLike_count()) / (float) influencerFollowers;
    }

    float calculateInfluencerEngagement(List<Float> postEngagements) {
        float total = 0;
        for (Float postEngagement : postEngagements) {
            total += postEngagement;
        }
        return total / postEngagements.size();
    }
}
