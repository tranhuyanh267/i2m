package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, String> {

}
