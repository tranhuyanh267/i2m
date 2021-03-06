package web.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user u SET u.img_url = ?2 WHERE u.id = ?1", nativeQuery = true)
    void updateImageUrl(String userId, String fileName);

    @Query(value = "SELECT * FROM user WHERE id = ?1", nativeQuery = true)
    User findUserByIdCustom(String id);

    @Query(value = "select * from user where role != 'ADMIN_ROLE' order  by full_name ",nativeQuery = true)
    Page<User> findAllUser(Pageable pageable);
}
