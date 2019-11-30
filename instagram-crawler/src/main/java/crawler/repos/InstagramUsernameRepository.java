package crawler.repos;

import crawler.documents.InstagramUsername;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramUsernameRepository extends CrudRepository<InstagramUsername, String> {
    List<InstagramUsername> findAll();
}
