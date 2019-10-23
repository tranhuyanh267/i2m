package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import crawler.entities.Post;
import crawler.repositories.PostRepository;
import crawler.utils.Transform;
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
public class InfluencerCreatedHandler {

    private Instagram4j instagram4j;
    private PostRepository postRepository;

    // @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_FETCH_LATEST_POST_QUEUE)
    public void handler(InfluencerCreatedEvent event) {
        try {
            InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(event.getInfluencerId()));
            InstagramFeedResult result = instagram4j.sendRequest(request);
            if (result.getItems() != null) {
                List<InstagramFeedItem> items = result.getItems();
                List<Post> posts = transformToEntities(items, event.getInfluencerId());
                postRepository.saveAll(posts);
            }
        } catch (Exception ex) {

        }
    }

    List<Post> transformToEntities(List<InstagramFeedItem> items, String influencerId) {
        return items.stream().map(item -> {
            String id = influencerId + "-" + items.indexOf(item) + "-1";
            return Transform.transform(item, id, "LATEST", influencerId);
        }).collect(Collectors.toList());
    }

}
