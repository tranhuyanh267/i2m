package common.events;

public class UsernameCreatedEvent extends Event {
    private String username;

    public UsernameCreatedEvent() {

    }

    public UsernameCreatedEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
