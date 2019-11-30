package crawler.repos;

import crawler.documents.InstagramUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramUserRepository extends CrudRepository<InstagramUser, String> {
}
