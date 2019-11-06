package crawler.handlers;

import common.events.InfluencerCreatedEvent;
import crawler.core.EventBus;
import crawler.entities.FeedPointer;
import crawler.entities.InstagramFeed;
import crawler.repos.FeedPointerRepo;
import crawler.repos.InstagramFeedRepo;
import lombok.AllArgsConstructor;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostHandler {

    private Instagram4j instagram4j;
    private EventBus eventBus;
    private InstagramFeedRepo instagramFeedRepo;
    private FeedPointerRepo feedPointerRepo;

    @RabbitListener(queues = "test")
    public void handler(InfluencerCreatedEvent event) {
        try {
            String influencerId = event.getInfluencerId();
            InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(influencerId), "", 0, 0);
            InstagramFeedResult result = instagram4j.sendRequest(request);
            if (!"ok".equals(result.getStatus())) {
                eventBus.emit(event);
                return;
            }

            List<InstagramFeedItem> items = result.getItems();
            if (items != null && items.size() > 0) {
                List<InstagramFeed> feeds = items.stream().map(InstagramFeed::new).collect(Collectors.toList());
                instagramFeedRepo.saveAll(feeds);

                FeedPointer pointer = new FeedPointer(influencerId, influencerId, result.getNext_max_id());
                feedPointerRepo.save(pointer);
            }
        } catch (Exception e) {

        }
    }

}
