package crawler.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Post {
    @Id
    private String id;
    private String code;
    private String content;
    private String thumbnailUrl;
    private boolean isVideo;
    private int viewCount;
    private int commentCount;
    private int likeCount;
    private String influencerId;
    private String type;
    private float engagement;

}

