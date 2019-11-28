package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.MailBox;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfessionRepository extends JpaRepository<MailBox, String> {

    Optional<MailBox> findByUserIdAndInfluencerId(String userId, String influencerId);

    @Query(value = "SELECT * FROM mail_box WHERE user_id = ?1", nativeQuery = true)
    List<MailBox> findConfessionsByUserId(String userId);

}
