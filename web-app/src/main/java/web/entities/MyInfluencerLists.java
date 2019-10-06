package web.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "myInfluencerLists")
public class MyInfluencerLists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public MyInfluencerLists() {
    }

    public MyInfluencerLists(String name) {
        this.name = name;
    }

    public MyInfluencerLists(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "influencer_myList", joinColumns = @JoinColumn(name = "listId"), inverseJoinColumns = @JoinColumn(name = "influencerId"))
    private Set<Influencers> influencers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Influencers> getInfluencers() {
        return influencers;
    }

    public void setInfluencers(Set<Influencers> influencers) {
        this.influencers = influencers;
    }
}
