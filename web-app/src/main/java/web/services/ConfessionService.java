package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entities.Confession;
import web.payload.AllConfessionResponse;
import web.repositories.ConfessionRepository;
import web.repositories.InfluencerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfessionService {
    @Autowired
    ConfessionRepository confessionRepository;

    @Autowired
    InfluencerRepository influencerRepository;

    public Confession findConfession(String userId, String influencerId) {
        return confessionRepository.findConfessionByUserIdAndInfluencerId(userId, influencerId);
    }

    public Confession createConfession(Confession confession) {
        return confessionRepository.save(confession);
    }

    public List<AllConfessionResponse> getAllUserConfession(String userId) {
        List<AllConfessionResponse> result = new ArrayList<>();
        List<Confession> allConfession = confessionRepository.findConfessionsByUserId(userId);
        for (Confession confession : allConfession) {
            AllConfessionResponse confessionInfo = new AllConfessionResponse(confession.getInfluencer().getUsername(), confession.getInfluencer().getProfilePicUrl(), confession.getId());
            result.add(confessionInfo);
        }
        return result;
    }

    public boolean checkConfessionExist(String userId, Long confessionId) {
        List<Confession> confessionList = confessionRepository.findConfessionsByUserId(userId);
        for (Confession confession : confessionList) {
            if (confession.getId() == confessionId) {
                return true;
            }
        }
        return false;
    }
}
