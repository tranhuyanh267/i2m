package common.events;

public class InfluencerEngagementCalculatedEvent extends Event {
    private String influencerId;
    private float engagement;

    public InfluencerEngagementCalculatedEvent() {

    }

    public InfluencerEngagementCalculatedEvent(String influencerId, float engagement) {
        this.influencerId = influencerId;
        this.engagement = engagement;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public float getEngagement() {
        return engagement;
    }

    public void setEngagement(float engagement) {
        this.engagement = engagement;
    }

    public String route() {
        return "influencerEngagementCalculated";
    }
}
