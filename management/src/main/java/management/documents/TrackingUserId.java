package management.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingUserId {
    @Id
    private String id;
    private String userId;
    private Set<String> categories;
}
