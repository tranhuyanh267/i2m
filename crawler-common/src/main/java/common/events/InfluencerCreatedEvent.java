package common.events;

public class InfluencerCreatedEvent extends Event {
    private String influencerId;

    public InfluencerCreatedEvent() {

    }

    public InfluencerCreatedEvent(String influencerId, int followers) {
        this.influencerId = influencerId;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public String route() {
        return "test";
    }
}
