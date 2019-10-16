package web.model;

import org.springframework.lang.Nullable;
import web.entities.Category;

import java.util.HashSet;
import java.util.Set;

public class UserUpdateModel {
    private String fullName;

    @Nullable
    private Set<Category> categories = new HashSet<>();

    public UserUpdateModel() {
    }

    public UserUpdateModel(String fullName, @Nullable Set<Category> categories) {
        this.fullName = fullName;
        this.categories = categories;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Nullable
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(@Nullable Set<Category> categories) {
        this.categories = categories;
    }
}
