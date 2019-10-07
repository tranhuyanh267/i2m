package management.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "tracking_usernames")
public class TrackingUsername {
    @Id
    private String id;
    private String username;
    private Set<String> categories;
}
