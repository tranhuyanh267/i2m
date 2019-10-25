package common.payload;

import common.events.Event;

public class PostPayload extends Event {
    private String id;
    private String code;
    private String content;
    private String thumbnailUrl;
    private boolean isVideo;
    private int viewCount;
    private int commentCount;
    private int likeCount;
    private float engagement;
    private String influencerId;
    private String type;

    public PostPayload() {

    }

    public PostPayload(String id, String code, String content, String thumbnailUrl, boolean isVideo, int viewCount, int commentCount, int likeCount, float engagement, String influencerId, String type) {
        this.id = id;
        this.code = code;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isVideo = isVideo;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.engagement = engagement;
        this.influencerId = influencerId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public float getEngagement() {
        return engagement;
    }

    public void setEngagement(float engagement) {
        this.engagement = engagement;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String route() {
        return "post";
    }

    @Override
    public String toString() {
        return "PostPayload{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", content='" + content + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", isVideo=" + isVideo +
                ", viewCount=" + viewCount +
                ", commentCount=" + commentCount +
                ", likeCount=" + likeCount +
                ", engagement=" + engagement +
                ", influencerId='" + influencerId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
