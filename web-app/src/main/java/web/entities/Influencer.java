package web.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table
@ToString
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
    private int userTagCount;
    private boolean hasAnonymousProfilePicture;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "influencer_category", joinColumns = @JoinColumn(name = "influencer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "influencer")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Post> posts = new ArrayList<>();

    @OneToMany(
            mappedBy = "influencer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Report> reports = new HashSet<>();


}
