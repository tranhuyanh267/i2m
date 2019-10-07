package management.repos;

import management.documents.TrackingUsername;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingInfluencerRepo extends CrudRepository<TrackingUsername, String> {
    List<TrackingUsername> findAll();
}
