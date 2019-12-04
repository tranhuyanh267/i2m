package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Influencer;
import web.entities.Pack;
import web.exceptions.WebAppException;
import web.payload.TopInfluencerResponse;
import web.repositories.CommentRepository;
import web.repositories.InfluencerRepository;
import web.repositories.PackRepository;

import java.util.*;


@Service
@AllArgsConstructor
public class InfluencerService {

    private InfluencerRepository influencerRepository;
    private PackRepository packRepository;
    private PackService packService;


    public String addInfluencerToPack(String influencerId, String packId) {
        Pack pack = packService.findById(packId);
        if(pack == null) {
            return "pack_not_found";
        }
        Optional<Influencer> influencerOpt = influencerRepository.findById(influencerId);

        if (influencerOpt.isPresent()) {
            if (pack != null) {
                if(pack.getInfluencers().contains(influencerOpt.get())) {
                    return "existing_influencer";
                }
                Influencer influencer = influencerOpt.get();
                pack.getInfluencers().add(influencer);
                pack.setInfluencers(pack.getInfluencers());
                packRepository.save(pack);
            }
        }
        return "";
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

    public Influencer getInfluencerDetail(String id) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow(() -> new WebAppException("User id not found " + id));
        influencer.getCategories().size();

        influencer.getPosts().forEach(item -> {
            item.setInfluencer(null);

        });

        return influencer;
    }
}
