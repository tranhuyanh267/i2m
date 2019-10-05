package web.payload;

import web.entities.Influencers;

import java.util.List;

public class InfluencerMyListResponse {
    private Long id;
    private String name;
    private List<Influencers> influencers;

    public InfluencerMyListResponse(Long id, String name, List<Influencers> influencers) {
        this.id = id;
        this.name = name;
        this.influencers = influencers;
    }

    public InfluencerMyListResponse() {
    }

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

    public List<Influencers> getInfluencers() {
        return influencers;
    }

    public void setInfluencers(List<Influencers> influencers) {
        this.influencers = influencers;
    }
}
