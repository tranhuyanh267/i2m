package common.events;

public class InfluencerCreatedEvent extends Event {
    private String influencerId;
    private int followers;

    public InfluencerCreatedEvent() {

    }

    public InfluencerCreatedEvent(String influencerId, int followers) {
        this.influencerId = influencerId;
        this.followers = followers;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String route() {
        return "influencerCreated";
    }
}
