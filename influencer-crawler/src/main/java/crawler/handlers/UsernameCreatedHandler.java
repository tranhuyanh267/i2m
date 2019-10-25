package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import common.events.UsernameCreatedEvent;
import crawler.core.EventBus;
import crawler.entities.InstagramUser;
import crawler.repos.InstagramUserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j
public class UsernameCreatedHandler {

    private EventBus eventBus;
    private Instagram4j instagram4j;
    private InstagramUserRepo instagramUserRepo;

    @RabbitListener(queues = QueueName.USERNAME_QUEUE)
    public void handler(UsernameCreatedEvent event) {
        if (StringUtils.isEmpty(event.getUsername())) {
            return;
        }
        try {
            InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(event.getUsername());
            InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
            if (!"ok".equals(result.getStatus()) && !"User not found".equals(result.getMessage())) {
                eventBus.emit(event);
                return;
            }
            InstagramUser instagramUser = new InstagramUser(result.getUser());
            if (!instagramUser.is_private()) {
                List<String> categories = event.getCategories().stream().map(this::getCategoryName).collect(Collectors.toList());
                categories.add(instagramUser.getCategory());
                instagramUser.setCategories(categories);
                instagramUserRepo.save(instagramUser);

                InfluencerCreatedEvent influencerCreatedEvent = new InfluencerCreatedEvent();
                influencerCreatedEvent.setInfluencerId(String.valueOf(instagramUser.getPk()));
                eventBus.emit(influencerCreatedEvent);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

//    Set<Category> buildCategoryList(List<String> cateStrings) {
//        return cateStrings.stream().map(item -> {
//            String categoryId = getCategoryId(item);
//            String categoryName = getCategoryName(item);
//            return new Category(categoryId, categoryName);
//        }).collect(Collectors.toSet());
//    }
//
//    private void storeReport(Influencer influencer) {
//        try {
//            Report report = new Report();
//            report.setCreatedDate(new Date());
//            report.setFollowers(influencer.getFollowers());
//            report.setType("FOLLOWERS");
//            report.setInfluencerId(influencer.getId());
//            reportRepository.save(report);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    Influencer transform(InstagramUser instagramUser) {
//        Influencer influencer = new Influencer();
//        influencer.setId(String.valueOf(instagramUser.getPk()));
//        influencer.setUsername(instagramUser.getUsername());
//        influencer.setFollowings(instagramUser.getFollowing_count());
//        influencer.setFollowers(instagramUser.getFollower_count());
//        influencer.setEmail(instagramUser.getPublic_email());
//        influencer.setBiography(instagramUser.getBiography());
//        influencer.setFullName(instagramUser.getFull_name());
//        influencer.setMediaCount(instagramUser.getMedia_count());
//        influencer.setVerified(instagramUser.is_verified());
//        influencer.setProfilePicUrl(instagramUser.getProfile_pic_url());
//        influencer.setExternalUrl(instagramUser.getExternal_url());
//        influencer.setHasAnonymousProfilePicture(instagramUser.isHas_anonymous_profile_picture());
//        influencer.setUserTagCount(instagramUser.getUsertags_count());
//        influencer.setAuthentic(instagramUser.is_verified());
//        return influencer;
//    }

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
