package common.payload;


import common.events.Event;

public class InfluencerPayload extends Event {
    private String id;
    private float engagement;
    private float averageLikePerPost;
    private float averageCommentPerPost;
    private float averageViewPerVideo;
    private float averageEngagementPerVideo;
    private float averageEngagementPerImage;


    public InfluencerPayload() {

    }

    public InfluencerPayload(String id, float engagement, float averageLikePerPost, float averageCommentPerPost, float averageViewPerVideo, float averageEngagementPerVideo, float averageEngagementPerImage) {
        this.id = id;
        this.engagement = engagement;
        this.averageLikePerPost = averageLikePerPost;
        this.averageCommentPerPost = averageCommentPerPost;

        this.averageViewPerVideo = averageViewPerVideo;
        this.averageEngagementPerVideo = averageEngagementPerVideo;

        this.averageEngagementPerImage = averageEngagementPerImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getEngagement() {
        return engagement;
    }

    public void setEngagement(float engagement) {
        this.engagement = engagement;
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

    public float getAverageEngagementPerImage() {
        return averageEngagementPerImage;
    }

    public void setAverageEngagementPerImage(float averageEngagementPerImage) {
        this.averageEngagementPerImage = averageEngagementPerImage;
    }

    public String route() {
        return "influencer";
    }
}
