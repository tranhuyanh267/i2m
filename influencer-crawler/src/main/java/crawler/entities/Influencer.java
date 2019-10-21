package crawler.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    private boolean isAuthentic;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "influencer_category",
            joinColumns = @JoinColumn(name = "influencer_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();

    public void addCategories(Category category) {
        categories.add(category);
        category.getInfluencers().add(this);
    }

    public void addCategories(Set<Category> categories) {
        categories.addAll(categories);
        categories.forEach(category -> category.getInfluencers().add(this));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

