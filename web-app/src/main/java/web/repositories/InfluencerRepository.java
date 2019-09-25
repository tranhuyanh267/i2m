package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Influencer;

import java.util.List;

@Repository
public interface InfluencerRepository extends CrudRepository<Influencer, String> {

    @Override
    List<Influencer> findAll();
}
