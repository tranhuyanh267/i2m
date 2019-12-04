package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

}
