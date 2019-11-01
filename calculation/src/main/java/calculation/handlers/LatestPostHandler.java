package calculation.handlers;

import calculation.documents.InstagramFeed;
import calculation.documents.InstagramUser;
import calculation.entities.Post;
import calculation.repos.InstagramFeedRepository;
import calculation.repos.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class LatestPostHandler {
    private InstagramFeedRepository instagramFeedRepository;
    private PostRepository postRepository;

    @RabbitListener(queues = "latest-post-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        List<InstagramFeed> lastestFeeds = instagramFeedRepository.findFirst12ByInstagramUserIdOrderByCreatedDateAsc(instagramUser.getId());
        if (lastestFeeds.size() > 0) {
            List<Post> latestPosts = new ArrayList<>();
            for (int i = 0; i < lastestFeeds.size(); i++) {
                Post post = new Post();
                BeanUtils.copyProperties(lastestFeeds.get(i), post);
                post.setId(instagramUser.getId() + "-" + (i + 1));
                post.setInfluencerId(instagramUser.getId());
                post.setType("LATEST");
                latestPosts.add(post);
            }
            postRepository.saveAll(latestPosts);
        }
    }
}
