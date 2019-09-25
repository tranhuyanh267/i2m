package web.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Influencer {
    @Id
    private String id;
    private String name;
    private String username;
    private String bio;
    private String avatarUrl;
    private int followers;
    private float engagement;
    private int likePerPost;
    private int avgLikePerPost;
    private int avgRepliesPerPost;
    private int avgViewsPerVideo;
    private int imageEngagement;
    private int videoEngagement;
    private float postPerDay;
    private float postPerWeek;
    private float estimatedPostValue;
}
