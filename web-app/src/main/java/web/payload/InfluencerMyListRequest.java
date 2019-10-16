package web.payload;

import lombok.Data;

@Data
public class InfluencerMyListRequest {
    private String packId;

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }
}
