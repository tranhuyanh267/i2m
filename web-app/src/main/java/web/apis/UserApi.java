package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.entities.MyInfluencerLists;
import web.entities.User;
import web.exceptions.WebAppException;
import web.payload.MyInfluencersRequest;
import web.repositories.MyInfluencersRepository;
import web.repositories.UserRepository;
import web.security.CurrentUser;
import web.security.UserPrincipal;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserApi {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyInfluencersRepository myInfluencersRepository;

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

    @PostMapping("/my-influencer")
    @PreAuthorize("hasRole('USER')")
    public MyInfluencerLists createMyInfluencerList(@Valid @RequestBody MyInfluencersRequest myInfluencersRequest, @CurrentUser UserPrincipal userPrincipal) {
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new WebAppException("User id not found " + userPrincipal.getId()));

        MyInfluencerLists newList = new MyInfluencerLists(myInfluencersRequest.getName(), currentUser);
        return myInfluencersRepository.save(newList);
    }

    @GetMapping("/my-influencer")
    @PreAuthorize("hasRole('USER')")
    public List<MyInfluencerLists> getMyInfluencers(@CurrentUser UserPrincipal userPrincipal) {
        return myInfluencersRepository.findByUserId(userPrincipal.getId());
    }

    @PutMapping("/my-influencer/{id}")
    @PreAuthorize("hasRole('USER')")
    public MyInfluencerLists updateMyInfluencerList(@PathVariable(value = "id") Long listId, @Valid @RequestBody MyInfluencerLists listDetail) {
        MyInfluencerLists list = myInfluencersRepository.findById(listId).orElseThrow(() -> new WebAppException("User id not found " + listId));
        list.setName(listDetail.getName());
        return myInfluencersRepository.save(list);
    }
}
