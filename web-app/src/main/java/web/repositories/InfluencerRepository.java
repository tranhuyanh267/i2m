package web.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.entities.Influencer;
import web.payload.TopInfluencerResponse;


import java.util.List;
import java.util.Optional;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, String> {

    @Query("select v from Influencer v where (v.followers between :minFollowers and :maxFollowers) and (v.engagement between :minEngagement and :maxEngagement) and (v.username like %:search% or v.fullName like %:search%)")
    Page<Influencer> filterInfluencer(@Param("search") String search,@Param("minFollowers") int minFollowers,
                                      @Param("maxFollowers") int maxFollowers, @Param("minEngagement") float minEngagement,
                                      @Param("maxEngagement") float maxEngagement, Pageable pageable);

    @Query(value = "select * from influencer i inner join influencer_category ic on ic.influencer_id = i.id where ic.category_id in (?5) and (i.followers between ?1 and ?2) and (i.engagement between ?3 and ?4) and  (i.username like %?6% or i.full_name like %?6%)", nativeQuery = true)
    Page<Influencer> filterInfluencerHasCategories(int minFollowers,
                                      int maxFollowers, float minEngagement,
                                      float maxEngagement, String[] categories,@Param("search") String search,  Pageable pageable);

    @Query("select v from Influencer v where v.username like %:search% or v.fullName like %:search%")
    Page<Influencer> findByUsernameAndFullName(@Param("search") String username, Pageable pageable);

    Optional<Influencer> findById(String id);

    @Query(value = "SELECT * FROM influencer i inner join influencer_category ic on ic.influencer_id = i.id where category_id in (?1) order by i.followers desc limit 9", nativeQuery = true)
    List<Influencer> findByUserCategory(List<String> categories);

    @Query(value = "select count(c.id) as mail_count, c.influencer_id, i.* from mail_box c inner join message m on m.mail_id = c.id inner join influencer i on i.id = c.influencer_id group by c.influencer_id order by mail_count desc, i.engagement desc", nativeQuery = true)
    Page<TopInfluencerResponse> findTopInfluencer(Pageable pageable);

    @Query(value = "select count(c.id) as mail_count, c.influencer_id, i.* from mail_box c inner join message m on m.mail_id = c.id inner join influencer i on i.id = c.influencer_id group by c.influencer_id order by mail_count desc, i.engagement desc limit 9", nativeQuery = true)
    List<Influencer> suggestTopInfluencer();
}
