package common.events;

public class UsernameCreatedEvent extends Event {
    private String username;
    private String category;

    public UsernameCreatedEvent() {

    }

    public UsernameCreatedEvent(String username, String category) {
        this.username = username;
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
