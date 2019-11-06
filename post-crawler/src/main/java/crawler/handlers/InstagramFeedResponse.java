package crawler.handlers;

import crawler.entities.InstagramFeed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramFeedResponse {
    private List<InstagramFeed> items;
    private String status;
    private boolean hasNextPage;
    private String maxId;
}
