package crawler.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
public class InstagramUsername {
    @Id
    private String id;
    private String username;
    private List<String> categories = new ArrayList<>();
}
