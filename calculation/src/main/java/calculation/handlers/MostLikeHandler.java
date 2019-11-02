package calculation.handlers;

import calculation.documents.InstagramFeed;
import calculation.documents.InstagramUser;
import calculation.entities.Post;
import calculation.repos.InstagramFeedRepository;
import calculation.repos.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j
public class MostLikeHandler {

    private InstagramFeedRepository instagramFeedRepository;
    private PostRepository postRepository;

    @RabbitListener(queues = "most-like-queue")
    public void handler(InstagramUser instagramUser) {
        log.info("Handle Most Like " + instagramUser.getId());
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        InstagramFeed mostLikeFeed = instagramFeedRepository.findFirstByInstagramUserIdOrderByLikeCountDesc(instagramUser.getId());
        if (mostLikeFeed != null) {
            Post post = new Post();
            BeanUtils.copyProperties(mostLikeFeed, post);
            post.setId(instagramUser.getId() + "-L");
            post.setInfluencerId(instagramUser.getId());
            post.setType("MOST_LIKE");
            postRepository.save(post);
        }
    }
}
