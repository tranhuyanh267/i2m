package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Influencers;

import java.util.List;

@Repository
public interface InfluencerRepository extends CrudRepository<Influencers, Long> {
//
//    @Override
//    List<Influencer> findAll();
}
