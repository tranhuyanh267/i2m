package calculation.repos;

import calculation.documents.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostRepository, String> {
    List<Post> findByInfluencerId(String influencerId);
}
