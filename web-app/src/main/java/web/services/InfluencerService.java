package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Influencer;
import web.repositories.InfluencerRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class InfluencerService {
    private InfluencerRepository influencerRepository;

    public List<Influencer> getInfluencers() {
        return this.influencerRepository.findAll();
    }

    public Influencer create(Influencer influencer) {
        return this.influencerRepository.save(influencer);
    }
}
