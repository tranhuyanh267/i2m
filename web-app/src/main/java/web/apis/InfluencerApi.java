package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.entities.Influencers;
import web.services.InfluencerService;

@RestController("api/influencers")
@AllArgsConstructor
public class InfluencerApi {
    private InfluencerService influencerService;

//    @GetMapping()
//    public List<Influencer> getInfluencers() {
//        return this.influencerService.getInfluencers();
//    }

    @PostMapping()
    public Influencers createInfluencer(@RequestBody() Influencers influencers) {
        return this.influencerService.create(influencers);
    }
}
