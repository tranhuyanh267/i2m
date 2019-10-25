package common.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsernameCreatedEvent extends Event {
    private String username;
    private List<String> categories = new ArrayList<String>();
    private Date crawTime;

    public UsernameCreatedEvent() {

    }

    public UsernameCreatedEvent(String username, List<String> categories, Date crawTime) {
        this.username = username;
        this.categories = categories;
        this.crawTime = crawTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Date getCrawTime() {
        return crawTime;
    }

    public void setCrawTime(Date crawTime) {
        this.crawTime = crawTime;
    }

    public String route() {
        return "username-queue";
    }
}
