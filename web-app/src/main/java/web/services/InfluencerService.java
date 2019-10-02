package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Influencers;
import web.repositories.InfluencerRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class InfluencerService {
    private InfluencerRepository influencerRepository;

//    public List<Influencer> getInfluencers() {
//        return this.influencerRepository.findAll();
//    }

    public Influencers create(Influencers influencer) {
        return this.influencerRepository.save(influencer);
    }
}
