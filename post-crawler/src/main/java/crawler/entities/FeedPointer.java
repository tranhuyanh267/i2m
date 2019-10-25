package crawler.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class FeedPointer {
    @Id
    private String id;
    private String influencerId;
    private String maxId;
    private Date createdDate;

    public FeedPointer(String id, String influencerId, String maxId) {
        this.id = id;
        this.influencerId = influencerId;
        this.maxId = maxId;
        this.createdDate = new Date();
    }
}
