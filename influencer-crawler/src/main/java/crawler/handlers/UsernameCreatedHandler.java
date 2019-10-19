package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import common.events.UsernameCreatedEvent;
import crawler.core.EventBus;
import crawler.entities.Category;
import crawler.entities.Influencer;
import crawler.entities.Report;
import crawler.repositories.InfluencerRepository;
import crawler.repositories.ReportRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j
public class UsernameCreatedHandler {

    private EventBus eventBus;
    private InfluencerRepository influencerRepository;
    private Instagram4j instagram4j;
    private ReportRepository reportRepository;

    @RabbitListener(queues = QueueName.USERNAME_QUEUE)
    public void handler(UsernameCreatedEvent event) {
        if (StringUtils.isEmpty(event.getUsername())) {
            return;
        }
        try {
            InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(event.getUsername());
            InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
            InstagramUser instagramUser = result.getUser();
            if (!"ok".equals(result.getStatus())) {
                UsernameCreatedEvent retryEvent = new UsernameCreatedEvent();
                retryEvent.setCategory(event.getCategory());
                retryEvent.setCrawTime(new Date());
                retryEvent.setUsername(event.getUsername());
                eventBus.emit(retryEvent);
                return;
            }
            if (instagramUser != null && !instagramUser.is_private()) {
                Influencer influencer = transform(instagramUser);
                String categoryId = getCategoryId(event.getCategory());
                String categoryName = getCategoryName(event.getCategory());
                influencer.addCategory(new Category(categoryId, categoryName));
                influencerRepository.save(influencer);

                storeReport(influencer);

                InfluencerCreatedEvent influencerCreatedEvent = new InfluencerCreatedEvent();
                influencerCreatedEvent.setInfluencerId(influencer.getId());
                influencerCreatedEvent.setFollowers(influencer.getFollowers());
                eventBus.emit(influencerCreatedEvent);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void storeReport(Influencer influencer) {
        try {
            Report report = new Report();
            report.setCreatedDate(new Date());
            report.setFollowers(influencer.getFollowers());
            report.setType("FOLLOWERS");
            report.setInfluencerId(influencer.getId());
            reportRepository.save(report);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    Influencer transform(InstagramUser instagramUser) {
        Influencer influencer = new Influencer();
        influencer.setId(String.valueOf(instagramUser.getPk()));
        influencer.setUsername(instagramUser.getUsername());
        influencer.setFollowings(instagramUser.getFollowing_count());
        influencer.setFollowers(instagramUser.getFollower_count());
        influencer.setEmail(instagramUser.getPublic_email());
        influencer.setBiography(instagramUser.getBiography());
        influencer.setFullName(instagramUser.getFull_name());
        influencer.setMediaCount(instagramUser.getMedia_count());
        influencer.setVerified(instagramUser.is_verified());
        influencer.setProfilePicUrl(instagramUser.getProfile_pic_url());
        influencer.setExternalUrl(instagramUser.getExternal_url());
        influencer.setHasAnonymousProfilePicture(instagramUser.isHas_anonymous_profile_picture());
        influencer.setUserTagCount(instagramUser.getUsertags_count());
        influencer.setAuthentic(instagramUser.is_verified());
        return influencer;
    }

    private String getCategoryName(String category) {
        return Arrays.stream(category.split("\\-"))
                .filter(s -> !s.equals("influencers"))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase() + " ")
                .collect(Collectors.joining());
    }

    private String getCategoryId(String category) {
        return category.replace("-influencers", "");
    }

}
