package crawler.repos;

import crawler.entities.InstagramFeed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramFeedRepo extends CrudRepository<InstagramFeed, String> {
}
