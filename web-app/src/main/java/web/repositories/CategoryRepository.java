package web.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
}
