package crawler.handlers;

import lombok.Data;

@Data
public class InstagramFeedRequest {
    private String userId;
    private String maxId;
}
