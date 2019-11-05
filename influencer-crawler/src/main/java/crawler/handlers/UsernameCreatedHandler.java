package crawler.handlers;

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

    @RabbitListener(queues = "username-queue")
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
            List<String> categories = event.getCategories().stream().map(this::getCategoryName).collect(Collectors.toList());
            categories.add(instagramUser.getCategory());
            instagramUser.setCategories(categories);
            instagramUserRepo.save(instagramUser);

            InfluencerCreatedEvent influencerCreatedEvent = new InfluencerCreatedEvent();
            influencerCreatedEvent.setInfluencerId(String.valueOf(instagramUser.getPk()));
            eventBus.emit(influencerCreatedEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getCategoryName(String category) {
        return Arrays.stream(category.split("\\-"))
                .filter(s -> !s.equals("influencers"))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase() + " ")
                .map(StringUtils::trimAllWhitespace)
                .collect(Collectors.joining());
    }

}
