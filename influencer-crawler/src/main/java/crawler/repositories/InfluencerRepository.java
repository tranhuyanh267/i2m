package crawler.repositories;

import crawler.entities.Influencer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends CrudRepository<Influencer, String> {
}
