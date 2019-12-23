package web.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Pack;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackRepository extends CrudRepository<Pack, String> {
    List<Pack> findByUserId(String userId);

    @Query(value = "select p.* from influencer_pack ip inner join pack p on ip.pack_id = p.id where influencer_id = ?1", nativeQuery = true)
    List<Pack> findByInfluencerId(String influencerId);

    Pack findByName(String name);

    Optional<Pack> findByNameAndUserId(String name, String userId);
}
