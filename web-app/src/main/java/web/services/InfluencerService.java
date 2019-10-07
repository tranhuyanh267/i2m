package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Influencer;
import web.entities.Pack;
import web.repositories.InfluencerRepository;
import web.repositories.PackRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InfluencerService {

    private InfluencerRepository influencerRepository;
    private PackRepository packRepository;
    private PackService packService;

    public Influencer addInfluencerToPack(String influencerId, String packId) {
        Pack pack = packService.findById(packId);
        Optional<Influencer> influencerOpt = influencerRepository.findById(influencerId);

        if (influencerOpt.isPresent()) {
            if (pack != null) {
                Influencer influencer = influencerOpt.get();
                pack.getInfluencers().add(influencer);
                pack.setInfluencers(pack.getInfluencers());
                packRepository.save(pack);
            }
        }
        return null;
    }
}
