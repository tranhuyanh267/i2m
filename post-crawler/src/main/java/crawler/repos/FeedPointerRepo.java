package crawler.repos;

import crawler.entities.FeedPointer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPointerRepo extends CrudRepository<FeedPointer, String> {
}
