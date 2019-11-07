package calculation.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document
@Data
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

