package calculation.dtos;

import lombok.Data;

@Data
public class Average {
    private float engagement;

    private float averageLikePerPost;
    private float averageCommentPerPost;

    private float averageViewPerVideo;
    private float averageEngagementPerVideo;

    private float averageEngagementPerImage;
}

