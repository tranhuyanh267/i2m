package common.events;

public class InfluencerCreatedEvent extends Event {
    private String influencerId;
    private String maxId;

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

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public String route() {
        return "test";
    }
}
