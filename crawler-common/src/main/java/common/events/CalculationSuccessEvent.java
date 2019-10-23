package common.events;

public class CalculationSuccessEvent extends Event {

    private float averageLikePerPost;
    private float averageCommentPerPost;
    private float averageEngagementPerPost;
    private float averageViewPerVideo;
    private float averageEngagementPerVideo;

    public CalculationSuccessEvent() {

    }

    public CalculationSuccessEvent(float averageLikePerPost, float averageCommentPerPost, float averageEngagementPerPost, float averageViewPerVideo, float averageEngagementPerVideo) {
        this.averageLikePerPost = averageLikePerPost;
        this.averageCommentPerPost = averageCommentPerPost;
        this.averageEngagementPerPost = averageEngagementPerPost;
        this.averageViewPerVideo = averageViewPerVideo;
        this.averageEngagementPerVideo = averageEngagementPerVideo;
    }


    public float getAverageLikePerPost() {
        return averageLikePerPost;
    }

    public void setAverageLikePerPost(float averageLikePerPost) {
        this.averageLikePerPost = averageLikePerPost;
    }

    public float getAverageCommentPerPost() {
        return averageCommentPerPost;
    }

    public void setAverageCommentPerPost(float averageCommentPerPost) {
        this.averageCommentPerPost = averageCommentPerPost;
    }

    public float getAverageEngagementPerPost() {
        return averageEngagementPerPost;
    }

    public void setAverageEngagementPerPost(float averageEngagementPerPost) {
        this.averageEngagementPerPost = averageEngagementPerPost;
    }

    public float getAverageViewPerVideo() {
        return averageViewPerVideo;
    }

    public void setAverageViewPerVideo(float averageViewPerVideo) {
        this.averageViewPerVideo = averageViewPerVideo;
    }

    public float getAverageEngagementPerVideo() {
        return averageEngagementPerVideo;
    }

    public void setAverageEngagementPerVideo(float averageEngagementPerVideo) {
        this.averageEngagementPerVideo = averageEngagementPerVideo;
    }

    public String route() {
        return "";
    }
}
