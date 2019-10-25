package crawler.repos;

import crawler.entities.InstagramUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramUserRepo extends CrudRepository<InstagramUser, String> {
}
