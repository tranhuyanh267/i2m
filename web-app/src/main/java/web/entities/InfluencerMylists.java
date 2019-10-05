package web.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "influencer_mylists")
public class InfluencerMylists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencers_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Influencers influencers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MyInfluencerLists myInfluencerLists;

    public InfluencerMylists() {
    }

    public InfluencerMylists(Influencers influencers, MyInfluencerLists myInfluencerLists) {
        this.influencers = influencers;
        this.myInfluencerLists = myInfluencerLists;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Influencers getInfluencers() {
        return influencers;
    }

    public void setInfluencers(Influencers influencers) {
        this.influencers = influencers;
    }

    public MyInfluencerLists getMyInfluencerLists() {
        return myInfluencerLists;
    }

    public void setMyInfluencerLists(MyInfluencerLists myInfluencerLists) {
        this.myInfluencerLists = myInfluencerLists;
    }
}
