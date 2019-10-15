package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.Messages;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query(value = "SELECT * FROM message where body like ?1 and subject like ?2", nativeQuery = true)
    Messages findByDetail(String oldBody, String oldSubject);

    @Query(value = "SELECT * FROM message WHERE conversation_id = ?1 ORDER BY send_date", nativeQuery = true)
    List<Messages> findByConfessionId(long confessionId);
}
