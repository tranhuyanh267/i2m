package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import crawler.entities.Post;
import crawler.repositories.PostRepository;
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

    @RabbitListener(queues = QueueName.INFLUENCER_QUEUE)
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
            try {
                Post post = new Post();
                post.setInfluencerId(influencerId);
                post.setId(item.getId());
                post.setCommentCount(item.getComment_count());
                post.setLikeCount(item.getLike_count());
                if (item.getCaption() != null) {
                    post.setContent(item.getCaption().getText());
                }
                post.setCode(item.getCode());
                post.setViewCount(item.getView_count());
                post.setVideo(item.isHas_audio());

                if (item.getImage_versions2() != null) {
                    post.setThumbnailUrl(item.getImage_versions2().getCandidates().get(0).getUrl());
                } else {
                    post.setThumbnailUrl(item.getCarousel_media().get(0).getImage_versions2().getCandidates().get(0).getUrl());
                }

                return post;
            } catch (Exception ex) {
                return null;
            }
        }).collect(Collectors.toList());
    }

}
