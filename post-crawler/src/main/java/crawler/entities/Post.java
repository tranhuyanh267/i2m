package crawler.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Post {
    @Id
    private String id;
    private String code;
    private int commentCount;
    private int likeCount;
    private String content;
    private String thumbnailUrl;
    private boolean isVideo;
    private int viewCount;
    @Column(name = "influencer_id")
    private String influencerId;
    private String type;
}

