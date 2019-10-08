package usernameworker;

import common.QueueName;
import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;
import payloads.InstagramUserPayload;

import java.io.IOException;

@Log4j
@SpringBootApplication
public class UserNameWorkerApplication {

    @Autowired
    private Instagram4j instagram4j;

    public static void main(String[] args) {
        SpringApplication.run(UserNameWorkerApplication.class, args);
    }

    @RabbitListener(queues = QueueName.USER_NAME_QUEUE)
    @SendTo(QueueName.USER_ID_QUEUE)
    public InstagramUserPayload handleUsername(InstagramUserPayload payload) throws IOException {
        log.info("crawing UserInfo handleUsername " + payload.getUsername());
        InstagramSearchUsernameRequest request = new InstagramSearchUsernameRequest(payload.getUsername());
        InstagramSearchUsernameResult result = instagram4j.sendRequest(request);
        InstagramUser instagramUser = result.getUser();
        if (instagramUser != null) {
            return transform(payload, instagramUser);
        }
        return null;
    }

    InstagramUserPayload transform(InstagramUserPayload payload, InstagramUser instagramUser) {
        payload.setUserId(String.valueOf(instagramUser.getPk()));
        payload.setFollowings(String.valueOf(instagramUser.getFollowing_count()));
        payload.setFollowers(String.valueOf(instagramUser.getFollower_count()));
        payload.setEmail(instagramUser.getPublic_email());
        payload.setBiography(instagramUser.getBiography());
        payload.setFullName(instagramUser.getFull_name());
        payload.setMediaCount(instagramUser.getMedia_count());
        payload.setVerified(instagramUser.is_verified());
        payload.setProfilePicUrl(instagramUser.getProfile_pic_url());
        payload.setExternalUrl(instagramUser.getExternal_url());
        return payload;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Instagram4j instagram4j() {
        Instagram4j instagram4j = Instagram4j.builder().username("huyanh267").password("huyanhkute").build();
        instagram4j.setup();
        try {
            instagram4j.login();
        } catch (IOException e) {
            log.error("Error while create instagram client");
        }
        return instagram4j;
    }

}
