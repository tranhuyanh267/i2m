package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entities.MailBox;
import web.payload.AllConfessionResponse;
import web.repositories.ConfessionRepository;
import web.repositories.InfluencerRepository;
import web.repositories.MessageRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfessionService {
    @Autowired
    ConfessionRepository confessionRepository;

    @Autowired
    InfluencerRepository influencerRepository;
    @Autowired
    private MessageRepository messageRepository;

    public MailBox findConfession(String userId, String influencerId) {
        return confessionRepository.findByUserIdAndInfluencerId(userId, influencerId).orElse(null);
    }

    public MailBox createConfession(MailBox confession) {
        return confessionRepository.save(confession);
    }

    public List<AllConfessionResponse> getAllUserConfession(String userId) {
        List<AllConfessionResponse> result = new ArrayList<>();
        List<MailBox> allConfession = confessionRepository.findConfessionsByUserId(userId);
        for (MailBox confession : allConfession) {
            AllConfessionResponse confessionInfo = new AllConfessionResponse(confession.getInfluencer().getUsername(), confession.getInfluencer().getProfilePicUrl(), confession.getId());
            result.add(confessionInfo);
        }
        return result;
    }

    public boolean checkConfessionExist(String userId, String confessionId) {
        List<MailBox> confessionList = confessionRepository.findConfessionsByUserId(userId);
        for (MailBox confession : confessionList) {
            if ((confession.getId()).equals(confessionId)) {
                return true;
            }
        }
        return false;
    }

}
