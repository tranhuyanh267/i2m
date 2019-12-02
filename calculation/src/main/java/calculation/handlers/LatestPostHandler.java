package calculation.handlers;

import calculation.documents.InstagramComment;
import calculation.documents.InstagramFeed;
import calculation.documents.InstagramUser;
import calculation.entities.Comment;
import calculation.entities.Influencer;
import calculation.entities.Post;
import calculation.repos.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j
public class LatestPostHandler {
    private InstagramFeedRepository instagramFeedRepository;
    private PostRepository postRepository;
    private InfluencerRepository influencerRepository;
    private RabbitTemplate rabbitTemplate;
    private CommentRepository commentRepository;
    private InstagramCommentRepository instagramCommentRepository;

    @RabbitListener(queues = "latest-post-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        try {
            log.info("Handle Latest Post " + instagramUser.getId());
            List<InstagramFeed> lastestFeeds = instagramFeedRepository.findFirst12ByInstagramUserIdOrderByTakenAtDesc(instagramUser.getId());
            List<Comment> commentBatch = new ArrayList<>();
            if (lastestFeeds.size() > 0) {
                List<Post> latestPosts = new ArrayList<>();
                for (int i = 0; i < lastestFeeds.size(); i++) {
                    InstagramFeed feed = lastestFeeds.get(i);
                    Post post = new Post();
                    BeanUtils.copyProperties(feed, post);
                    post.setId(instagramUser.getId() + "-" + (i + 1));
                    post.setInfluencerId(instagramUser.getId());
                    post.setType("LATEST");
                    latestPosts.add(post);


                    List<InstagramComment> comments = instagramCommentRepository.findByFeedId(feed.getId());
                    if (!CollectionUtils.isEmpty(comments)) {
                        List<Comment> collect = comments.stream().map(comment -> {
                            Comment c = new Comment();
                            c.setId(comment.getId());
                            c.setContent(comment.getContent());
                            c.setPostId(post.getId());
                            return c;
                        }).collect(Collectors.toList());
                        commentBatch.addAll(collect);
                    }
                }
                commentRepository.saveAll(commentBatch);
                postRepository.saveAll(latestPosts);

                Optional<Influencer> influencerOptional = influencerRepository.findById(instagramUser.getId());
                if (influencerOptional.isPresent()) {
                    Influencer influencer = influencerOptional.get();
                    influencer.setLastPostTakenAt(lastestFeeds.get(0).getTakenAt());
                    influencerRepository.save(influencer);
                }
            }
        } catch (Exception ex) {
            rabbitTemplate.convertAndSend("latest-post-queue", instagramUser);
        }
    }
}
