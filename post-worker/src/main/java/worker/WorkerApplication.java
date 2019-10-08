package worker;

import common.QueueName;
import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;
import payloads.InstagramUserPayload;
import payloads.PostPayload;

import java.io.IOException;
import java.util.stream.Collectors;


@Log4j
@SpringBootApplication
public class WorkerApplication {

    @Autowired
    private Instagram4j instagram4j;


    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

    @RabbitListener(queues = QueueName.USER_ID_QUEUE)
    @SendTo(QueueName.INFLUENCER_QUEUE)
    public InstagramUserPayload handleUserId(InstagramUserPayload instagramUserPayload) throws IOException {
        log.info("Handling " + instagramUserPayload.getUserId());
        InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(instagramUserPayload.getUserId()));
        InstagramFeedResult result = instagram4j.sendRequest(request);
        if (result.getItems() != null) {
            instagramUserPayload.setPosts(result.getItems().stream().map(this::transform).collect(Collectors.toList()));
        }
        return instagramUserPayload;
    }


    PostPayload transform(InstagramFeedItem instagramFeedItem) {
        PostPayload postPayload = new PostPayload();
        postPayload.setCommentCount(instagramFeedItem.getComment_count());
        postPayload.setLikeCount(instagramFeedItem.getLike_count());
        if (instagramFeedItem.getCaption() != null) {
            postPayload.setContent(instagramFeedItem.getCaption().getText());
        }
        postPayload.setCode(instagramFeedItem.getCode());
        postPayload.setViewCount(instagramFeedItem.getView_count());
        postPayload.setVideo(instagramFeedItem.isHas_audio());
        try {
            postPayload.setThumbnailUrl(instagramFeedItem.getCarousel_media().get(0).getImage_versions2().getCandidates().get(0).getUrl());
        } catch (Exception e) {

        }
        return postPayload;
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

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
