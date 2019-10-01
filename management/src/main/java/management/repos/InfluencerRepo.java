package management.repos;

import management.documents.Influencer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfluencerRepo extends CrudRepository<Influencer, String> {
    List<Influencer> findAll();
}
