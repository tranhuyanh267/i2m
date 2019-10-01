package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
