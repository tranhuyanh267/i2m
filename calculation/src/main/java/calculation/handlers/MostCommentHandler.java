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
public class MostCommentHandler {
    private InstagramFeedRepository instagramFeedRepository;
    private PostRepository postRepository;

    @RabbitListener(queues = "most-comment-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        log.info("Handle Most Comment " + instagramUser.getId());
        InstagramFeed mostCommentFeed = instagramFeedRepository.findFirstByInstagramUserIdOrderByCommentCountDesc(instagramUser.getId());
        if (mostCommentFeed != null) {
            Post post = new Post();
            BeanUtils.copyProperties(mostCommentFeed, post);
            post.setId(instagramUser.getId() + "-C");
            post.setInfluencerId(instagramUser.getId());
            post.setType("MOST_COMMENT");
            postRepository.save(post);
        }
    }
}
