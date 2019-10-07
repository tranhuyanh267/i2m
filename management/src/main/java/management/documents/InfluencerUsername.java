package management.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "influencerUsername")
public class InfluencerUsername {
    @Id
    private String id;
    private String username;
    private String category;
}
