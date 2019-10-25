package calculation.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private float engagement;
    private String influencerId;
    private String type;

}
