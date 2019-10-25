package management.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
public class Post {

    @Id
    private String id;
    private String code;
    private int commentCount;
    private int likeCount;
    private String type;

    @Lob
    private String content;

    @Lob
    private String thumbnailUrl;
    private boolean isVideo;
    private int viewCount;
    private String influencerId;

}
