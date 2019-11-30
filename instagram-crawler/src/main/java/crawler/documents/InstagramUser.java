package crawler.documents;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class InstagramUser extends org.brunocvcunha.instagram4j.requests.payload.InstagramUser {

    @Id
    private String id;
    private List<String> categories = new ArrayList<>();

    public InstagramUser(org.brunocvcunha.instagram4j.requests.payload.InstagramUser instagramUser) {
        BeanUtils.copyProperties(instagramUser, this);
        this.id = String.valueOf(instagramUser.getPk());
        this.addCategory(instagramUser.getCategory());
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public void addCategories(List<String> categories) {
        this.categories.addAll(categories);
    }
}
