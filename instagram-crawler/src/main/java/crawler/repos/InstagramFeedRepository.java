package crawler.repos;

import crawler.documents.InstagramFeed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramFeedRepository extends CrudRepository<InstagramFeed, String> {
}
