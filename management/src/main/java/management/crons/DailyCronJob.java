package management.crons;

import management.documents.Influencer;
import management.repos.InfluencerRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyCronJob {

    private InfluencerRepo influencerRepo;

    @Scheduled
    public void updateTrackingInfluencers() {
        List<Influencer> influencers = influencerRepo.findAll();
        //push to queue
    }
}
