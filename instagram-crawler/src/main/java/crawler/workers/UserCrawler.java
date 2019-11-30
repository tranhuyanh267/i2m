package crawler.workers;

import crawler.documents.InstagramUser;
import crawler.messages.UserIdMessage;
import crawler.messages.UsernameMessage;
import crawler.repos.InstagramUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class UserCrawler {

    private InstagramUserRepository instagramUserRepository;
    private Instagram4j instagram4j;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "username-queue")
    public void handler(UsernameMessage message) {
        if (StringUtils.isEmpty(message.getUsername())) {
            return;
        }
        try {
            InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(message.getUsername());
            InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
            if ("User not found".equals(result.getMessage())) {
                log.error(result.getMessage());
                return;
            }
            if (!"ok".equals(result.getStatus()) && !"User not found".equals(result.getMessage())) {
                log.error(result.getMessage());
                rabbitTemplate.convertAndSend("username-queue", message);
                return;
            }

            InstagramUser instagramUser = new InstagramUser(result.getUser());
            List<String> categories = message.getCategories().stream().map(this::getCategoryName).collect(Collectors.toList());
            categories.add(instagramUser.getCategory());
            instagramUser.setCategories(categories);
            if (instagramUser.getCategory() == null || instagramUser.getCategory().length() == 0) {
                instagramUser.setCategories(Collections.singletonList("Special"));
            }
            instagramUserRepository.save(instagramUser);

            UserIdMessage userIdMessage = new UserIdMessage();
            userIdMessage.setUserId(String.valueOf(instagramUser.getPk()));
            rabbitTemplate.convertAndSend("user-id-queue", userIdMessage);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend("username-queue", message);
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
