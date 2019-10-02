package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.entities.User;
import web.exceptions.WebAppException;
import web.repositories.UserRepository;
import web.security.CurrentUser;
import web.security.UserPrincipal;

@RestController
@RequestMapping("api/users")
public class UserApi {
    @Autowired
    private UserRepository userRepository;

//    private UserService userService;
//
//    @GetMapping("self")
//    public User getUserProfile() {
//        return this.userService.getCurrentUserProfile();
//    }
//
//    @PostMapping("register")
//    public void register(@RequestBody() User user) {
//        this.userService.register(user);
//    }
//
//    @PutMapping("self")
//    public void update() {
//
//    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new WebAppException("User id not found " + userPrincipal.getId()));
    }

}
