package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.entities.Category;
import web.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryApi {
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getCategory() {
        return categoryService.findAll();
    }
}
