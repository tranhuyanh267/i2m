package calculation.repos;

import calculation.documents.InstagramComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramCommentRepository extends CrudRepository<InstagramComment, String> {
    List<InstagramComment> findByFeedId(String feedId);
}
