package web.payload;

import lombok.Data;
import org.springframework.lang.Nullable;
import web.entities.Category;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Data
public class SignUpRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    @Nullable
    private Set<Category> category = new HashSet<>();

}
