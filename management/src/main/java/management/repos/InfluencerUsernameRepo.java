package management.repos;

import management.documents.InfluencerUsername;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfluencerUsernameRepo extends CrudRepository<InfluencerUsername, String> {
    List<InfluencerUsername> findAll();
}
