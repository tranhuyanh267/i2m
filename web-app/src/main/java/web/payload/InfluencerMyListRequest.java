package web.payload;

import javax.validation.constraints.NotBlank;

public class InfluencerMyListRequest {

    private Long influencerId;

    public Long getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(Long influencerId) {
        this.influencerId = influencerId;
    }
}
