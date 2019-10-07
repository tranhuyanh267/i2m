package web.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import web.entities.Influencer;


@Repository
public interface InfluencerRepository extends PagingAndSortingRepository<Influencer, String> {
}
