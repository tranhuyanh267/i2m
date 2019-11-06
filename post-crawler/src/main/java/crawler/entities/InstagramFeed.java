package crawler.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "instagramFeed")
public class InstagramFeed {
    @Id
    private String id;
    private String code;
    private int commentCount;
    private int likeCount;
    private int viewCount;
    private String content;
    private String thumbnailUrl;
    private String instagramUserId;
    private Date takenAt;
    private Date createdDate;
}
