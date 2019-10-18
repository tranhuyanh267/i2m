package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerEngagementCalculatedEvent;
import crawler.entities.Influencer;
import crawler.entities.Report;
import crawler.repositories.InfluencerRepository;
import crawler.repositories.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class InfluencerEngagementCalculatedHandler {

    private InfluencerRepository influencerRepository;
    private ReportRepository reportRepository;

    @RabbitListener(queues = QueueName.INFLUENCER_WAITING_TO_SAVE_ENGAGEMENT_QUEUE)
    public void handler(InfluencerEngagementCalculatedEvent event) {
        Optional<Influencer> influencerOpt = influencerRepository.findById(event.getInfluencerId());
        if (influencerOpt.isPresent()) {
            try {
                Influencer influencer = influencerOpt.get();
                influencer.setEngagement(event.getEngagement());
                influencerRepository.save(influencer);

                storeReport(influencer);
            } catch(Exception ex) {

            }
        }
    }

    private void storeReport(Influencer influencer) {
        try {
            Report report = new Report();
            report.setCreatedDate(new Date());
            report.setEngagement(influencer.getEngagement());
            report.setType("ENGAGEMENT");
            report.setInfluencerId(influencer.getId());
            reportRepository.save(report);
        } catch (Exception e) {
        }
    }
}
