package management.repos;

import management.entities.Influencer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepo extends CrudRepository<Influencer, String> {
}
