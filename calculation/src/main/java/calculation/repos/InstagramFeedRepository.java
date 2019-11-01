package calculation.repos;

import calculation.documents.InstagramFeed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramFeedRepository extends MongoRepository<InstagramFeed, String> {
    List<InstagramFeed> findByInstagramUserId(String instagramUserId);

    List<InstagramFeed> findFirst12ByInstagramUserIdOrderByCreatedDateAsc(String instagramUserId);

    InstagramFeed findFirstByInstagramUserIdOrderByLikeCountDesc(String instagramUserId);

    InstagramFeed findFirstByInstagramUserIdOrderByCommentCountDesc(String instagramUserId);

}
