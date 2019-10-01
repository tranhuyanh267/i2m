package web.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
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
