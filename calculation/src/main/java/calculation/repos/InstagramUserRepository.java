package calculation.repos;

import calculation.documents.InstagramUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramUserRepository extends MongoRepository<InstagramUser, String> {
    List<InstagramUser> findAll();
}
