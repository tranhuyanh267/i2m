package calculation.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
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
    private int userTagCount;

    @Lob
    private String profilePicUrl;
    private String email;

    @Lob
    private String externalUrl;
    private boolean isVerified;
    private boolean isAuthentic;
    private boolean isPrivate;
    private float engagement;
    private float averageLikePerPost;
    private float averageCommentPerPost;
    private float averageViewPerVideo;
    private float averageEngagementPerVideo;
    private float averageEngagementPerImage;
    private Date lastPostTakenAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "influencer_category", joinColumns = @JoinColumn(name = "influencer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public void setCategories(Set<Category> categories) {
        this.categories.addAll(categories);
        categories.forEach(ca -> ca.getInfluencers().add(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        return id != null && id.equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
