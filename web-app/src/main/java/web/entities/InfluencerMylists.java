package web.entities;

import javax.persistence.*;

@Entity
@Table(name = "influencer_mylists")
public class InfluencerMylists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencers_id")
    private Influencers influencers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private MyInfluencerLists myInfluencerLists;

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
