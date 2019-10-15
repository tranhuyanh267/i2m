package web.payload;

public class AllConfessionResponse {
    private String influencerUsername;
    private String profilePicUrl;
    private Long confessionId;

    public AllConfessionResponse(String influencerUsername, String profilePicUrl, Long confessionId) {
        this.influencerUsername = influencerUsername;
        this.profilePicUrl = profilePicUrl;
        this.confessionId = confessionId;
    }

    public String getInfluencerUsername() {
        return influencerUsername;
    }

    public void setInfluencerUsername(String influencerUsername) {
        this.influencerUsername = influencerUsername;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Long getConfessionId() {
        return confessionId;
    }

    public void setConfessionId(Long confessionId) {
        this.confessionId = confessionId;
    }
}
