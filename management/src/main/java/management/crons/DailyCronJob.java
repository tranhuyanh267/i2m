package management.crons;

import common.QueueName;
import lombok.AllArgsConstructor;
import management.documents.TrackingUserId;
import management.repos.TrackingUserIdRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DailyCronJob {

    private static final String DAILY = "*/5 * * * * ?";

    private TrackingUserIdRepo trackingUserIdRepo;
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = DAILY)
    public void updateTrackingInfluencers() {
        List<TrackingUserId> trackingUserIds = trackingUserIdRepo.findAll();
        trackingUserIds.forEach(influencer -> {
            rabbitTemplate.convertAndSend(QueueName.USER_ID_QUEUE, influencer);
        });
    }

}
