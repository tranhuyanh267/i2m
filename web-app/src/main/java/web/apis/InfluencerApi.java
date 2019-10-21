package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import web.constants.AppConstants;
import web.entities.Category;
import web.entities.Influencer;
import web.entities.User;
import web.exceptions.WebAppException;
import web.payload.InfluencerMyListRequest;
import web.payload.PagedResponse;
import web.payload.TopInfluencerResponse;
import web.repositories.InfluencerRepository;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.InfluencerService;
import web.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/influencers")
public class InfluencerApi {

    private InfluencerRepository influencerRepository;
    private InfluencerService influencerService;
    private UserService userService;

    @GetMapping
    public PagedResponse<Influencer> getInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                    @RequestParam(value = "sortBy", defaultValue = "followers") String sortBy,
                                                    @RequestParam(value = "search", defaultValue = "") String search,
                                                    @Nullable @RequestParam(value = "minFollowers") Integer minFollowers,
                                                    @Nullable @RequestParam(value = "maxFollowers") Integer maxFollowers,
                                                    @Nullable @RequestParam(value = "minEngagement") Float minEngagement,
                                                    @Nullable @RequestParam(value = "maxEngagement") Float maxEngagement,
                                                    @Nullable @RequestParam(value = "categories[]") String[] categories
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);

        if (maxFollowers == null) {
            Page<Influencer> infulenerLists = influencerRepository.findByUsernameAndFullName(search, pageable);
            return normalizeResponse(infulenerLists);
        }

            if(categories == null) {
                Page<Influencer> infulenerLists = influencerRepository.filterInfluencer(search, minFollowers, maxFollowers, minEngagement, maxEngagement, pageable);
                return normalizeResponse(infulenerLists);
            }

            Page<Influencer> infulenerLists = influencerRepository.filterInfluencerHasCategories(minFollowers, maxFollowers, minEngagement, maxEngagement, categories, search, pageable);
            return normalizeResponse(infulenerLists);

    }

    @GetMapping("/{id}")
    public Influencer getInfluencerDetail(@PathVariable(value = "id") String influencerId) {
        Influencer influencer = influencerRepository.findById(influencerId).orElseThrow(() -> new WebAppException("User id not found " + influencerId));
        influencer.getCategories().size();

        influencer.getPosts().forEach(item -> item.setInfluencer(null));
        return influencer;

    }

    @PutMapping("/{id}")
    public Influencer addToPack(@PathVariable(value = "id") String influencerId, @RequestBody InfluencerMyListRequest influencerMyListRequest) {
        return this.influencerService.addInfluencerToPack(influencerId, influencerMyListRequest.getPackId());
    }


    @GetMapping("/suggestion")
    public List<Influencer> getTopInfluencer(@CurrentUser UserPrincipal userPrincipal) {
        if(userPrincipal != null) {
            User userDetails = userService.getUserDetails(userPrincipal.getId());


            if(userDetails.getCategories().size() > 0) {
                List<String> categoryIds = userDetails.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList());
                return influencerRepository.findByUserCategory(categoryIds);
            }
        }

        List<Influencer> influencers = influencerRepository.suggestTopInfluencer();
        if(influencers.size() > 0) {
            return influencers;
        }
        return influencerRepository.findOrderByFollowersDescLimitTo(9);
    }

    private PagedResponse<Influencer> normalizeResponse(Page<Influencer> infulenerLists) {
        List<Influencer> result = infulenerLists.getContent().stream().map(item -> {
            item.getCategories().size();
            item.setPosts(null);
            return item;
        }).collect(Collectors.toList());

        return new PagedResponse<>(result, infulenerLists.getNumber(), infulenerLists.getSize(), infulenerLists.getTotalElements(), infulenerLists.getTotalPages(), infulenerLists.isLast());
    }

    @GetMapping("/ranking")
    public PagedResponse<TopInfluencerResponse> findTopInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        Page<TopInfluencerResponse> influencerResponses = influencerService.findTopInfluencer(page, size);

        return new PagedResponse<>(influencerResponses.getContent(), influencerResponses.getNumber(), influencerResponses.getSize(), influencerResponses.getTotalElements(), influencerResponses.getTotalPages(), influencerResponses.isLast());
    }
}
