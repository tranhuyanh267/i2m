package web.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.entities.Influencer;


import java.util.Optional;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, String> {

    @Query("select v from Influencer v where (v.followers between :minFollowers and :maxFollowers) and (v.engagement between :minEngagement and :maxEngagement) and (v.username like %:search% or v.fullName like %:search%)")
    Page<Influencer> filterInfluencer(@Param("search") String username, @Param("minFollowers") int minFollowers,
                                      @Param("maxFollowers") int maxFollowers, @Param("minEngagement") float minEngagement, @Param("maxEngagement") float maxEngagement, Pageable pageable);

    @Query("select v from Influencer v where v.username like %:search% or v.fullName like %:search%")
    Page<Influencer> findByUsernameAndFullName(@Param("search") String username, Pageable pageable);

    Optional<Influencer> findById(String id);
}
