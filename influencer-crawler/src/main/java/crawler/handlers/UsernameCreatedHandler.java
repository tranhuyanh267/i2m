package crawler.handlers;

import common.constants.QueueName;
import common.events.InfluencerCreatedEvent;
import common.events.UsernameCreatedEvent;
import crawler.core.EventBus;
import crawler.entities.Category;
import crawler.entities.Influencer;
import crawler.repositories.InfluencerRepository;
import lombok.AllArgsConstructor;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsernameCreatedHandler {

    private EventBus eventBus;
    private InfluencerRepository influencerRepository;
    private Instagram4j instagram4j;

    @RabbitListener(queues = QueueName.USERNAME_QUEUE)
    public void handler(UsernameCreatedEvent event) {
        if (StringUtils.isEmpty(event.getUsername())) {
            return;
        }
        try {
            InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(event.getUsername());
            InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
            InstagramUser instagramUser = result.getUser();
            if (instagramUser != null && !instagramUser.is_private()) {
                Influencer influencer = transform(instagramUser);
                String categoryId = getCategoryId(event.getCategory());
                String categoryName = getCategoryName(event.getCategory());
                influencer.addCategory(new Category(categoryId, categoryName));
                influencerRepository.save(influencer);

                InfluencerCreatedEvent influencerCreatedEvent = new InfluencerCreatedEvent();
                influencerCreatedEvent.setInfluencerId(influencer.getId());
                eventBus.emit(influencerCreatedEvent);
            }
        } catch (Exception e) {

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
