package crawler.repos;

import crawler.documents.InstagramComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramCommentRepository extends CrudRepository<InstagramComment, String> {
}
