package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.entities.User;
import web.services.UserService;

@AllArgsConstructor
@RestController("api/users")
public class UserApi {

    private UserService userService;

    @GetMapping("self")
    public User getUserProfile() {
        return this.userService.getCurrentUserProfile();
    }

    @PostMapping("register")
    public void register(@RequestBody() User user) {
        this.userService.register(user);
    }

    @PutMapping("self")
    public void update() {

    }
}
