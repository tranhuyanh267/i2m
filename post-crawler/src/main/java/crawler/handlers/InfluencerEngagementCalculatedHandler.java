package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import crawler.entities.Post;
import crawler.utils.Transform;
import lombok.AllArgsConstructor;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InfluencerEngagementCalculatedHandler {

    private Instagram4j instagram4j;
    private MongoTemplate mongoTemplate;

    @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_CALCULATE_ENGAGEMENT_QUEUE)
    public void handler(InfluencerCreatedEvent event) {
        if (event.getFollowers() == 0) {
            return;
        }
        try {
            String influencerId = event.getInfluencerId();
            String maxId = "";
            do {
                InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(influencerId), maxId, 0, 0);
                InstagramFeedResult result = instagram4j.sendRequest(request);
                if (result.getItems() != null) {
                    List<InstagramFeedItem> items = result.getItems();
                    List<Post> posts = items.stream().map(item -> {
                        Post post = Transform.transform(item, item.getId(), "", influencerId);
                        if (post != null) {
                            post.setEngagement(calculateEngagement(post, event.getFollowers()));
                        }
                        return post;
                    }).collect(Collectors.toList());
                    mongoTemplate.insertAll(posts);
                }
                maxId = result.getNext_max_id();
            }
            while (maxId != null);
        } catch (Exception e) {

        }
    }

    private float calculateEngagement(Post post, int followers) {
        try {
            if (post.isVideo()) {
                return (post.getCommentCount() + post.getLikeCount() + post.getViewCount()) / (float) followers;
            }
            return (post.getLikeCount() + post.getCommentCount()) / (float) followers;
        } catch (Exception ex) {
            return 0;
        }
    }

}
