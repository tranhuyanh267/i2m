package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.MailBox;

import java.util.List;

@Repository
public interface ConfessionRepository extends JpaRepository<MailBox, String> {

    @Query(value = "SELECT * FROM mail_box AS c WHERE c.user_id = ?1 AND c.influencer_id = ?2", nativeQuery = true)
    MailBox findConfessionByUserIdAndInfluencerId(String userId, String influencerId);

    @Query(value = "SELECT * FROM mail_box WHERE user_id = ?1", nativeQuery = true)
    List<MailBox> findConfessionsByUserId(String userId);
}
