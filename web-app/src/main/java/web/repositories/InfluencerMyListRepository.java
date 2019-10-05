package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.entities.InfluencerMylists;
import web.entities.Influencers;

import java.util.List;

@Repository
public interface InfluencerMyListRepository extends JpaRepository<InfluencerMylists, Long> {

    @Query("Select v.influencers from InfluencerMylists v where v.myInfluencerLists.id = :listId")
    List<Influencers> findByMyInfluencerListsId(@Param("listId") Long listId);

    Boolean existsByMyInfluencerListsIdAndInfluencersId(Long listId, Long influencerId);
}
