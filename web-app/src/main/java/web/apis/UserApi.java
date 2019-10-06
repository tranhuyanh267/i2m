package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.entities.Influencers;
import web.entities.MyInfluencerLists;
import web.entities.User;
import web.exceptions.WebApiReponse;
import web.exceptions.WebAppException;
import web.payload.ApiResponse;
import web.payload.InfluencerMyListRequest;
import web.payload.MyInfluencersRequest;
import web.repositories.InfluencerRepository;
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


    @Autowired
    private InfluencerRepository influencerRepository;

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
    public MyInfluencerLists renameInfluencerList(@PathVariable(value = "id") Long listId, @Valid @RequestBody MyInfluencersRequest myInfluencersRequest) {
        MyInfluencerLists list = myInfluencersRepository.findById(listId).orElseThrow(() -> new WebAppException("My influencer id not found " + listId));
        list.setName(myInfluencersRequest.getName());
        return myInfluencersRepository.save(list);
    }

    @DeleteMapping("/my-influencer/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteMyList(@PathVariable(value = "id") Long listId) {
        MyInfluencerLists list = myInfluencersRepository.findById(listId).orElseThrow(() -> new WebAppException("My influencer id not found " + listId));
        myInfluencersRepository.delete(list);
        return ResponseEntity.ok(new ApiResponse(true, null));
    }

    @GetMapping("/my-influencer/{id}")
    @PreAuthorize("hasRole('USER')")
    public MyInfluencerLists getMyListDetail(@PathVariable(value = "id") Long listId) {
        MyInfluencerLists list = myInfluencersRepository.findById(listId).orElseThrow(() -> new WebAppException("My influencer id not found " + listId));

        return list;
    }

    @PostMapping("/my-influencer/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity saveInfluencerToList(@PathVariable(value = "id") Long listId, @Valid @RequestBody InfluencerMyListRequest influencerMyListRequest) {
        MyInfluencerLists list = myInfluencersRepository.findById(listId).orElseThrow(() -> new WebAppException("My influencer id not found " + listId));
        Influencers influencers = influencerRepository.findById(influencerMyListRequest.getInfluencerId()).orElseThrow(() -> new WebAppException("Influencer id not found " + influencerMyListRequest.getInfluencerId()));

        if(list.getInfluencers().contains(influencers)) {
            return ResponseEntity.badRequest().body(new WebApiReponse(false, "existing_influencer"));
        }

        list.getInfluencers().add(influencers);
        list.setInfluencers(list.getInfluencers());


        return ResponseEntity.ok(myInfluencersRepository.save(list));
    }

}
