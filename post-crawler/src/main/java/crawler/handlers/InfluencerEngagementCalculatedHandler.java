package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import common.events.InfluencerEngagementCalculatedEvent;
import crawler.core.EventBus;
import crawler.repositories.PostRepository;
import crawler.utils.Transform;
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
    private PostRepository postRepository;

    @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_CALCULATE_ENGAGEMENT_QUEUE)
    public void handler(InfluencerCreatedEvent event) {
        if (event.getFollowers() == 0) {
            return;
        }
        try {
            String influencerId = event.getInfluencerId();
            List<Float> postEngagements = new ArrayList<>();
            String maxId = "";
            int highestLike = 0;
            InstagramFeedItem highestLikePost = null;
            InstagramFeedItem highestCommentPost = null;
            int highestComment = 0;
            do {
                InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(influencerId), maxId, 0, 0);
                InstagramFeedResult result = instagram4j.sendRequest(request);
                if (result.getItems() != null) {
                    List<InstagramFeedItem> items = result.getItems();
                    for (InstagramFeedItem item : items) {
                        if (item.getLike_count() > highestLike) {
                            highestLike = item.getLike_count();
                            highestLikePost = item;
                        }
                        if (item.getComment_count() > highestComment) {
                            highestComment = item.getComment_count();
                            highestCommentPost = item;
                        }
                        float postEngagement = calculatePostEngagement(item, event.getFollowers());
                        postEngagements.add(postEngagement);
                    }
                }
                maxId = result.getNext_max_id();
            }
            while (maxId != null && postEngagements.size() < 50);
            if (highestLikePost != null) {
                postRepository.save(Transform.transform(highestLikePost, influencerId + "-2", "MOST_LIKE", influencerId));
            }
            if (highestCommentPost != null) {
                postRepository.save(Transform.transform(highestCommentPost, influencerId+ "-3", "MOST_COMMENT", influencerId));
            }

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
        if (postEngagements.size() == 0) {
            return 0;
        }
        for (Float postEngagement : postEngagements) {
            total += postEngagement;
        }
        return total / postEngagements.size();
    }
}
