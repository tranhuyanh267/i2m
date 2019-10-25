package calculation.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "influencers")
public class Influencer {
    @Id
    private String id;
    private String pk;
}
