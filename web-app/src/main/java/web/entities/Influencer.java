package web.entities;


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
    private String biography;
    private String followers;
    private String followings;
    private int mediaCount;
    private String profilePicUrl;
    private String email;
    private String externalUrl;
    private boolean isVerified;
    private float engagement;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "influencer_category", joinColumns = @JoinColumn(name = "influencer_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "influencer")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "influencer_pack", joinColumns = @JoinColumn(name = "influencer_id"), inverseJoinColumns = @JoinColumn(name = "pack_id"))
    private List<Pack> packs = new ArrayList<>();

    public void addPack(Pack pack) {
        this.getPacks().add(pack);
    }

}
