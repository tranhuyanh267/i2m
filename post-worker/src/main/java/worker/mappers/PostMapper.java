package worker.mappers;

import instagram.model.PostInfo;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;

public interface PostMapper {
    PostInfo toPost(InstagramFeedItem instagramFeedItem);
}
