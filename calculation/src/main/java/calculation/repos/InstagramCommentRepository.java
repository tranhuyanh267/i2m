package calculation.repos;

import calculation.documents.InstagramComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramCommentRepository extends MongoRepository<InstagramComment, String> {
    List<InstagramComment> findByFeedId(String feedId);
}
