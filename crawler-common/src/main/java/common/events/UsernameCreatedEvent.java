package common.events;

import java.util.Date;

public class UsernameCreatedEvent extends Event {
    private String username;
    private String category;
    private Date crawTime;

    public UsernameCreatedEvent() {

    }

    public UsernameCreatedEvent(String username, String category, Date crawTime) {
        this.username = username;
        this.category = category;
        this.crawTime = crawTime;
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

    public Date getCrawTime() {
        return crawTime;
    }

    public void setCrawTime(Date crawTime) {
        this.crawTime = crawTime;
    }
}
