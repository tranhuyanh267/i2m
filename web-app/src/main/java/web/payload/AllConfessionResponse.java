package web.payload;

import lombok.Data;

@Data
public class AllConfessionResponse {
    private String influencerUsername;
    private String profilePicUrl;
    private String confessionId;

    public AllConfessionResponse(String influencerUsername, String profilePicUrl, String confessionId) {
        this.influencerUsername = influencerUsername;
        this.profilePicUrl = profilePicUrl;
        this.confessionId = confessionId;
    }


}
