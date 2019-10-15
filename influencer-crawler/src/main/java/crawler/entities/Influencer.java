package crawler.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Influencer {
    @Id
    private String id;
    private String fullName;
    private String username;
    private String biography;
    private int followers;
    private int followings;
    private int mediaCount;
    private float engagement;
    private String profilePicUrl;
    private String email;
    private boolean isVerified;
    private String externalUrl;
    private boolean hasAnonymousProfilePicture;
    private int userTagCount;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "influencer_category",
            joinColumns = @JoinColumn(name = "influencer_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();

    public void addCategory(Category tag) {
        categories.add(tag);
        tag.getInfluencers().add(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

