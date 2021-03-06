package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {

    @Query(value = "SELECT * FROM message where body like ?1 and subject like ?2", nativeQuery = true)
    Message findByDetail(String oldBody, String oldSubject);
    
    @Query(value = "SELECT * FROM message WHERE mail_id = ?1 ORDER BY send_date desc", nativeQuery = true)
    List<Message> findByConfessionId(String confessionId);

    @Query(value = "SELECT * FROM message where subject like ?1", nativeQuery = true)
    List<Message> findBySubject(String subject);

    @Query(value = "SELECT * FROM message WHERE file_url like ?1", nativeQuery = true)
    Message findByFileUrl(String filename);
}
