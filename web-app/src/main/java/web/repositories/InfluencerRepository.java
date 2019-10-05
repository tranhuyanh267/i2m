package web.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.entities.Influencers;


@Repository
public interface InfluencerRepository extends JpaRepository<Influencers, Long> {

    Page<Influencers> findAll(Pageable pageable);
}
