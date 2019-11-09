package calculation.handlers;

import calculation.core.EventBus;
import calculation.documents.InstagramFeed;
import calculation.documents.InstagramUser;
import calculation.dtos.Average;
import calculation.entities.Influencer;
import calculation.repos.InfluencerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
@AllArgsConstructor
@Log4j
class AverageCalculationVideoHandler {
    private MongoTemplate mongoTemplate;
    private InfluencerRepository influencerRepository;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "average-video-calculation-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        try {
            log.info("Handle Average Calculation Video " + instagramUser.getId());
            MatchOperation match = match(new Criteria("instagramUserId").is(instagramUser.getId())
                    .andOperator(new Criteria("viewCount").gt(0)));

            ProjectionOperation project = project("instagramUserId", "viewCount")
                    .andExpression("(likeCount + commentCount) / " + instagramUser.getFollowers()).as("engagement");

            GroupOperation group = group("instagramUserId")
                    .avg("viewCount").as("averageViewPerVideo")
                    .avg("engagement").as("averageEngagementPerVideo");

            TypedAggregation agg = new TypedAggregation(InstagramFeed.class, match, project, group);
            Average average = mongoTemplate.aggregate(agg, Average.class).getUniqueMappedResult();
            if (average != null) {
                Optional<Influencer> influencerOptional = influencerRepository.findById(instagramUser.getId());
                if (influencerOptional.isPresent()) {
                    Influencer influencer = influencerOptional.get();
                    influencer.setAverageViewPerVideo(average.getAverageViewPerVideo());
                    influencer.setAverageEngagementPerVideo(average.getAverageEngagementPerVideo());
                    influencerRepository.save(influencer);
                }
            }
        } catch (Exception ex) {
            rabbitTemplate.convertAndSend("average-video-calculation-queue", instagramUser);
        }
    }
}
