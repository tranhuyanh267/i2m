package management.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Influencer {
    @Id
    private String id;
    private String username;
    private String fullName;

    @Lob
    private String biography;
    private int followers;
    private int followings;
    private int mediaCount;

    @Lob
    private String profilePicUrl;
    private String email;

    @Lob
    private String externalUrl;
    private boolean isVerified;

    private float engagement;
    private float averageLikePerPost;
    private float averageCommentPerPost;
    private float averageViewPerVideo;
    private float averageEngagementPerVideo;
    private float averageEngagementPerImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "influencer_category", joinColumns = @JoinColumn(name = "influencer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

}
