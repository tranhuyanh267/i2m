package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import web.constants.AppConstants;
import web.entities.Influencer;
import web.exceptions.WebAppException;
import web.payload.InfluencerMyListRequest;
import web.payload.PagedResponse;
import web.repositories.InfluencerRepository;
import web.services.InfluencerService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/influencers")
public class InfluencerApi {

    private InfluencerRepository influencerRepository;
    private InfluencerService influencerService;

    @GetMapping
    public PagedResponse<Influencer> getInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                    @RequestParam(value = "sortBy", defaultValue = "followers") String sortBy,
                                                    @RequestParam(value = "search", defaultValue = "") String search,
                                                    @Nullable @RequestParam(value = "minFollowers") Integer minFollowers,
                                                    @Nullable @RequestParam(value = "maxFollowers") Integer maxFollowers,
                                                    @Nullable @RequestParam(value = "minEngagement") Float minEngagement,
                                                    @Nullable @RequestParam(value = "maxEngagement") Float maxEngagement
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);

        if (maxFollowers == null || minFollowers == null || minEngagement == null || maxEngagement == null) {
            Page<Influencer> infulenerLists = influencerRepository.findByUsernameAndFullName(search, pageable);
            return normalizeResponse(infulenerLists);
        }

        Page<Influencer> infulenerLists = influencerRepository.filterInfluencer(search, minFollowers, maxFollowers, minEngagement, maxEngagement, pageable);

        return normalizeResponse(infulenerLists);

    }

    @GetMapping("/{id}")
    public Influencer getInfluencerDetail(@PathVariable(value = "id") String influencerId) {
        Influencer influencer = influencerRepository.findById(influencerId).orElseThrow(() -> new WebAppException("User id not found " + influencerId));
        influencer.getPosts().forEach(item -> item.setInfluencer(null));
        return influencer;

    }

    @PutMapping("/{id}")
    public Influencer addToPack(@PathVariable(value = "id") String influencerId, @RequestBody InfluencerMyListRequest influencerMyListRequest) {
        return this.influencerService.addInfluencerToPack(influencerId, influencerMyListRequest.getPackId());
    }

//    @GetMapping("/max-followers")
//    public Integer findMaxFollowers() {
//        return influencerRepository.findTopByOrderByFollowersDesc().getFollowers();
//    }

    private PagedResponse<Influencer> normalizeResponse(Page<Influencer> infulenerLists) {
        List<Influencer> result = infulenerLists.getContent().stream().map(item -> {
            item.setPosts(null);
            return item;
        }).collect(Collectors.toList());
        return new PagedResponse<>(result, infulenerLists.getNumber(), infulenerLists.getSize(), infulenerLists.getTotalElements(), infulenerLists.getTotalPages(), infulenerLists.isLast());
    }


}
