package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.entities.MyInfluencerLists;

import java.util.List;

@Repository
public interface MyInfluencersRepository extends JpaRepository<MyInfluencerLists, Long> {
    @Query("Select v from MyInfluencerLists v where v.user.id = :userId")
    List<MyInfluencerLists> findByUserId(@Param("userId") Long userId);
}
