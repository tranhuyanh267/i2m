package payloads;

import lombok.Data;

@Data
public class PostPayload {
    private String code;
    private int commentCount;
    private int likeCount;
    private String content;
    private String thumbnailUrl;
    private boolean isVideo;
    private int viewCount;
}
