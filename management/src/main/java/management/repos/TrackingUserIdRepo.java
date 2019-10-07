package management.repos;

import management.documents.TrackingUserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingUserIdRepo extends CrudRepository<TrackingUserId, String> {
    List<TrackingUserId> findAll();
}
