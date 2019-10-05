package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.entities.Influencers;
import web.exceptions.WebAppException;
import web.payload.PagedResponse;
import web.repositories.InfluencerRepository;
import web.util.AppConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/influencers")
public class InfluencerApi {

    @Autowired
    private InfluencerRepository influencerRepository;


    @GetMapping()
    public PagedResponse<Influencers> getInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Influencers> infulenerLists = influencerRepository.findAll(pageable);
        List<Influencers> result = infulenerLists.getContent();
        return new PagedResponse<>(result, infulenerLists.getNumber(), infulenerLists.getSize(), infulenerLists.getTotalElements(), infulenerLists.getTotalPages(), infulenerLists.isLast());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Influencers getInfluencerDetail(@PathVariable(value = "id") Long influencerId) {
        return influencerRepository.findById(influencerId).orElseThrow(() -> new WebAppException("User id not found " + influencerId));

    }

}
