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

    @Query("select v from Influencer v where v.isAuthentic = true and (v.followers between :minFollowers and :maxFollowers) and (v.engagement between :minEngagement and :maxEngagement) and (v.username like %:search% or v.fullName like %:search%)")
    Page<Influencer> filterInfluencer(@Param("search") String search,@Param("minFollowers") int minFollowers,
                                      @Param("maxFollowers") int maxFollowers, @Param("minEngagement") float minEngagement,
                                      @Param("maxEngagement") float maxEngagement, Pageable pageable);

    @Query(value = "select * from influencer i inner join influencer_category ic on ic.influencer_id = i.id where ic.category_id in (?5) and (i.followers between ?1 and ?2) and (i.engagement between ?3 and ?4) and  (i.username like %?6% or i.full_name like %?6%) and is_authentic = true", nativeQuery = true)
    Page<Influencer> filterInfluencerHasCategories(int minFollowers,
                                      int maxFollowers, float minEngagement,
                                      float maxEngagement, String[] categories,@Param("search") String search,  Pageable pageable);

    @Query(value = "select * from influencer v where (v.username like %?1% or v.full_name like %?1%) and v.is_authentic = true", nativeQuery = true)
    Page<Influencer> findByUsernameAndFullName(String username, Pageable pageable);

    Optional<Influencer> findById(String id);

    @Query(value = "SELECT * FROM influencer i inner join influencer_category ic on ic.influencer_id = i.id where category_id in (?1) order by i.followers desc limit 9", nativeQuery = true)
    List<Influencer> findByUserCategory(List<String> categories);

    @Query(value = "select * from (select i.full_name, mail_count, i.username, i.email, i.profile_pic_url, i.id, i.followers, i.followings, i.average_like_per_post, i.average_comment_per_post, i.engagement, (i.average_like_per_post*0.2 + i.average_comment_per_post*0.2 + i.engagement*0.3 + ifnull(mail_count, 0)*0.3) / datediff(now(), last_post_taken_at) as weight from influencer i " +
            "left outer join (select count(c.id) as mail_count, c.influencer_id as c_influencer_id from mail_box c " +
            "inner join message m on m.mail_id = c.id group by c.influencer_id) n on i.id = n.c_influencer_id) v " +
            "order by weight desc limit 10", nativeQuery = true)
    List<TopInfluencerResponse> findTopInfluencer();


    @Query(value = "select * from influencer order by followers desc limit ?1", nativeQuery = true)
    List<Influencer> findOrderByFollowersDescLimitTo(int limit);

    @Query(value = "select * from influencer where followers > 1000 limit 5000", nativeQuery = true)
    List<Influencer> filterInfluencer();

}
