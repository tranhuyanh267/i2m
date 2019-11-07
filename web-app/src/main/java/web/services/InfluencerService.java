package web.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import web.entities.Influencer;
import web.entities.Pack;
import web.payload.TopInfluencerResponse;
import web.repositories.InfluencerRepository;
import web.repositories.PackRepository;

import java.util.List;
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

    public boolean checkInfluencerEmail(String influencerId) {
        Influencer currentInfluencer = influencerRepository.findById(influencerId).orElse(null);
        //Invalid Id
        if(currentInfluencer == null){
            return false;
        }
        try {
            String influencerEmail = currentInfluencer.getEmail();
            if (influencerEmail.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<TopInfluencerResponse> findTopInfluencer(int page, int size) {
        return influencerRepository.findTopInfluencer();
    }
}
