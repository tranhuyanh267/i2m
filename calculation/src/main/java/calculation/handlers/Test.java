package calculation.handlers;

import calculation.core.EventBus;
import calculation.documents.Post;
import calculation.repos.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class Test {

    private final PostRepository postRepository;
    private final EventBus eventBus;

    public void calculate(String influencerId, int influencerFollowers) {
        int totalLike = 0, totalComment = 0, totalView = 0, totalEngagement = 0, totalVideo = 0, totalImage = 0, totalPost = 0;

        float averageLikePerPost, averageCommentPerPost, averageEngagementPerPost, averageViewPerVideo, averageEngagementPerVideo;
        Post mostLike, mostComment, mostEngagement;

        int mostLikeValue = 0;
        int mostCommentValue = 0;
        float mostEngagementValue = 0;
        List<Post> posts = postRepository.findByInfluencerId(influencerId);
        totalPost = posts.size();
        for (Post post : posts) {
            totalLike += post.getLikeCount();
            totalComment += post.getCommentCount();
            totalEngagement += post.getEngagement();
            totalView += post.getViewCount();
            if (post.isVideo()) {
                totalVideo++;
            } else {
                totalImage++;
            }
            if (post.getLikeCount() > mostLikeValue) {
                mostLikeValue = post.getLikeCount();
                mostLike = post;
            }

            if (post.getCommentCount() > mostCommentValue) {
                mostCommentValue = post.getCommentCount();
                mostComment = post;
            }

            if (post.getEngagement() > mostEngagementValue) {
                mostEngagementValue = post.getEngagement();
                mostEngagement = post;
            }
        }

        averageLikePerPost = totalLike / totalPost;
        averageCommentPerPost = totalComment / totalPost;
        averageEngagementPerPost = totalEngagement / totalPost;
        averageViewPerVideo = totalView / totalVideo;
        averageEngagementPerVideo = totalView / totalVideo;

    }
}
