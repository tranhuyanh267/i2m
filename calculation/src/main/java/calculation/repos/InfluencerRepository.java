package calculation.repos;

import calculation.documents.Influencer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfluencerRepository extends CrudRepository<Influencer, String> {
    List<Influencer> findAll();
}
