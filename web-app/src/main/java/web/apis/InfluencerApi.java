package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                                                    @RequestParam(value = "sortBy", defaultValue = "followers") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);
        Page<Influencer> infulenerLists = influencerRepository.findAll(pageable);
        List<Influencer> result = infulenerLists.getContent().stream().map(item -> {
            item.setPosts(null);
            return item;
        }).collect(Collectors.toList());
        return new PagedResponse<>(result, infulenerLists.getNumber(), infulenerLists.getSize(), infulenerLists.getTotalElements(), infulenerLists.getTotalPages(), infulenerLists.isLast());
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


}
