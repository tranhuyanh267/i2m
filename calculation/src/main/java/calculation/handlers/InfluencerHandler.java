package calculation.handlers;

import calculation.core.EventBus;
import calculation.documents.InstagramUser;
import calculation.entities.Category;
import calculation.entities.Influencer;
import calculation.entities.Report;
import calculation.repos.InfluencerRepository;
import calculation.repos.InstagramUserRepository;
import calculation.repos.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class InfluencerHandler {
    private InfluencerRepository influencerRepository;
    private ReportRepository reportRepository;
    private EventBus eventBus;
    private InstagramUserRepository instagramUserRepository;

    @RabbitListener(queues = "instagram-user-queue")
    public void handler(InstagramUser instagramUser) {
        if (instagramUser.getMediaCount() <= 0 || instagramUser.isPrivate() || instagramUser.getFollowers() <= 0) {
            return;
        }
        Optional<Influencer> influencerOptional = influencerRepository.findById(instagramUser.getId());
        if (influencerOptional.isPresent()) {
            Influencer influencer = influencerOptional.get();
            BeanUtils.copyProperties(instagramUser, influencer);
            Set<Category> collect = instagramUser.getCategories().stream().map(ca -> {
                Category category = new Category();
                category.setId(ca.replace(" ", "-").substring(0, ca.replace(" ", "-").lastIndexOf("-") - 1).toLowerCase());
                category.setName(ca);
                return category;
            }).collect(Collectors.toSet());
            influencer.setCategories(collect);
            influencerRepository.save(influencer);

            eventBus.emit("influencer-exchange", instagramUser);

            Report report = new Report();
            report.setFollowers(instagramUser.getFollowers());
            report.setType("FOLLOWERS");
            report.setInfluencerId(influencer.getId());
            report.setCreatedDate(new Date());
            reportRepository.save(report);
        }
    }
}