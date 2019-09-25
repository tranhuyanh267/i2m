package worker.mappers;

import instagram.model.PostInfo;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.springframework.stereotype.Component;

@Component
public class PostMappingImp implements PostMapper {

    @Override
    public PostInfo toPost(InstagramFeedItem instagramFeedItem) {
        PostInfo postInfo = new PostInfo();
        return postInfo;
    }
}
