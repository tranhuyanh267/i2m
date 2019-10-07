package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.entities.Pack;
import web.entities.User;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.InfluencerService;
import web.services.PackService;
import web.services.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserApi {

    private UserService userService;
    private PackService packService;
    private InfluencerService influencerService;

    @GetMapping("/self")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User userDetails = userService.getUserDetails(userPrincipal.getId());
        userDetails.setPassword(null);
        return userDetails;
    }

    @GetMapping("/self/packs")
    public List<Pack> getMyPacks(@CurrentUser UserPrincipal userPrincipal) {
        return this.packService.getUserPacks(userPrincipal.getId());
    }


}
