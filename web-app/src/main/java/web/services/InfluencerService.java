package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Influencer;
import web.entities.Pack;
import web.repositories.InfluencerRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InfluencerService {

    private InfluencerRepository influencerRepository;
    private PackService packService;

    public Influencer addInfluencerToPack(String influencerId, String packId) {
        Optional<Influencer> influencerOpt = influencerRepository.findById(influencerId);
        if (influencerOpt.isPresent()) {
            Pack pack = packService.findById(packId);
            if (pack != null) {
                Influencer influencer = influencerOpt.get();
                influencer.addPack(pack);
                influencerRepository.save(influencer);
            }
        }
        return null;
    }
}
