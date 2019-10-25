package calculation.handlers;

import calculation.core.EventBus;
import calculation.documents.Post;
import calculation.repos.PostRepository;
import common.payload.InfluencerPayload;
import common.payload.PostPayload;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class Test {

    private final PostRepository postRepository;
    private final EventBus eventBus;

    public void calculate(String influencerId, int influencerFollowers) {
        int totalLike = 0, totalComment = 0, totalView = 0, totalVideo = 0, totalImage = 0, totalPost = 0;

        float averageLikePerPost = 0;
        float averageCommentPerPost = 0, averageEngagementPerPost = 0, averageViewPerVideo = 0, averageEngagementPerVideo = 0, totalEngagement = 0;
        PostPayload mostLike = new PostPayload();
        PostPayload mostComment = new PostPayload();
        PostPayload mostEngagement = new PostPayload();
        float totalVideoEngagement = 0;
        float totalImageEngagement = 0;

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
                totalVideoEngagement += post.getEngagement();
            } else {
                totalImage++;
                totalImageEngagement += post.getEngagement();
            }
            if (post.getLikeCount() > mostLikeValue) {
                mostLikeValue = post.getLikeCount();
                BeanUtils.copyProperties(post, mostLike);
            }

            if (post.getCommentCount() > mostCommentValue) {
                mostCommentValue = post.getCommentCount();
                BeanUtils.copyProperties(post, mostComment);
            }

            if (post.getEngagement() > mostEngagementValue) {
                mostEngagementValue = post.getEngagement();
                BeanUtils.copyProperties(post, mostEngagement);
            }
        }

        float engagement = 0;
        float averageEngagementPerImage = 0;

        if (totalPost > 0) {
            averageLikePerPost = totalLike / (float) totalPost;
            averageCommentPerPost = totalComment / (float) totalPost;
            engagement = totalEngagement / totalPost;
        }

        if (totalVideo > 0) {
            averageViewPerVideo = totalView / (float) totalVideo;
            averageEngagementPerVideo = totalVideoEngagement / totalVideo;
        }

        if (totalImage > 0) {
            averageEngagementPerImage = totalImageEngagement / totalImage;
        }


        InfluencerPayload influencerPayload = new InfluencerPayload();
        influencerPayload.setId(influencerId);
        influencerPayload.setAverageCommentPerPost(averageCommentPerPost);
        influencerPayload.setAverageLikePerPost(averageLikePerPost);
        influencerPayload.setAverageViewPerVideo(averageViewPerVideo);
        influencerPayload.setAverageEngagementPerVideo(averageEngagementPerVideo);
        influencerPayload.setAverageEngagementPerImage(averageEngagementPerImage);
        influencerPayload.setEngagement(engagement);

        mostLike.setType("MOST_LIKE");
        mostComment.setType("MOST_COMMENT");
        mostEngagement.setType("MOST_ENGAGEMENT");

        mostLike.setId(influencerId + "-L");
        mostComment.setId(influencerId + "-C");
        mostEngagement.setId(influencerId + "-E");

        eventBus.emit(influencerPayload);
        eventBus.emit(mostLike);
        eventBus.emit(mostComment);
        eventBus.emit(mostEngagement);

        int count = 0;
        for (Post post : posts) {
            count++;
            PostPayload payload = new PostPayload();
            BeanUtils.copyProperties(post, payload);
            payload.setType("LATEST");
            payload.setId(influencerId + '-' + count);
            eventBus.emit(payload);
            if (count > 12) {
                break;
            }
        }
    }
}
