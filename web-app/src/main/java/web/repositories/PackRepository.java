package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Pack;

import java.util.List;

@Repository
public interface PackRepository extends CrudRepository<Pack, String> {
    List<Pack> findByUserId(String userId);
}
