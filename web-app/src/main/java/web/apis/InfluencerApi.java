package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.entities.Influencer;
import web.services.InfluencerService;

import java.util.List;

@RestController("api/influencers")
@AllArgsConstructor
public class InfluencerApi {
    private InfluencerService influencerService;

    @GetMapping()
    public List<Influencer> getInfluencers() {
        return this.influencerService.getInfluencers();
    }

    @PostMapping()
    public Influencer createInfluencer(@RequestBody() Influencer influencer) {
        return this.influencerService.create(influencer);
    }
}
