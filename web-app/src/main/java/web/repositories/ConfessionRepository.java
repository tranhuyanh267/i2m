package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.Confession;

import java.util.List;

@Repository
public interface ConfessionRepository extends JpaRepository<Confession, Long> {

    @Query(value = "SELECT * FROM confession AS c WHERE c.user_id = ?1 AND c.influencer_id = ?2", nativeQuery = true)
    Confession findConfessionByUserIdAndInfluencerId(String userId, String influencerId);

    @Query(value = "SELECT * FROM confession WHERE user_id = ?1", nativeQuery = true)
    List<Confession> findConfessionsByUserId(String userId);

}
