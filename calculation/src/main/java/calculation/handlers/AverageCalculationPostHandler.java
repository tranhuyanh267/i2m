package calculation.handlers;

import calculation.documents.InstagramFeed;
import calculation.documents.InstagramUser;
import calculation.dtos.Average;
import calculation.entities.Influencer;
import calculation.repos.InfluencerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
public class AverageCalculationPostHandler {
    private MongoTemplate mongoTemplate;
    private InfluencerRepository influencerRepository;

    @RabbitListener(queues = "average-post-calculation-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }

        log.info("Handle Average Calculation Post  " + instagramUser.getId());
        MatchOperation match = match(new Criteria("instagramUserId").is(instagramUser.getId()));
        ProjectionOperation project = project("likeCount", "commentCount", "instagramUserId")
                .andExpression("(likeCount + viewCount + commentCount) / " + instagramUser.getFollowers()).as("engagement");

        GroupOperation group = group("instagramUserId")
                .avg("likeCount").as("averageLikePerPost")
                .avg("commentCount").as("averageCommentPerPost")
                .avg("engagement").as("engagement");

        TypedAggregation agg = new TypedAggregation(InstagramFeed.class, match, project, group);
        Average average = mongoTemplate.aggregate(agg, Average.class).getUniqueMappedResult();

        if (average != null) {
            Optional<Influencer> influencerOptional = influencerRepository.findById(instagramUser.getId());
            if (influencerOptional.isPresent()) {
                Influencer influencer = influencerOptional.get();
                BeanUtils.copyProperties(average, influencer);
                influencerRepository.save(influencer);
            }
        }

    }
}
