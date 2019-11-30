package crawler.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class InstagramComment {
    @Id
    private String id;
    private String content;
    private String postId;
}
