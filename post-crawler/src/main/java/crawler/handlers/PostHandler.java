package crawler.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.events.InfluencerCreatedEvent;
import crawler.core.EventBus;
import crawler.entities.InstagramFeed;
import crawler.repos.FeedPointerRepo;
import crawler.repos.InstagramFeedRepo;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostHandler {

    private Instagram4j instagram4j;
    private EventBus eventBus;
    private InstagramFeedRepo instagramFeedRepo;
    private FeedPointerRepo feedPointerRepo;

//    @RabbitListener(queues = "test")
//    public void handler(InfluencerCreatedEvent event) {
//        try {
//            String influencerId = event.getInfluencerId();
//            InstagramUserFeedRequest request = new InstagramUserFeedRequest(Long.valueOf(influencerId), "", 0, 0);
//            InstagramFeedResult result = instagram4j.sendRequest(request);
//            if (!"ok".equals(result.getStatus())) {
//                eventBus.emit(event);
//                return;
//            }
//
//            List<InstagramFeedItem> items = result.getItems();
//            if (items != null && items.size() > 0) {
//                List<InstagramFeed> feeds = items.stream().map(InstagramFeed::new).collect(Collectors.toList());
//                instagramFeedRepo.saveAll(feeds);
//
//                FeedPointer pointer = new FeedPointer(influencerId, influencerId, result.getNext_max_id());
//                feedPointerRepo.save(pointer);
//            }
//        } catch (Exception e) {
//
//        }
//    }

    @RabbitListener(queues = "test")
    public void handler(InfluencerCreatedEvent event) throws IOException {
        try {
            System.out.println("craw post of " + event.getInfluencerId());
            String influencerId = event.getInfluencerId();

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet("https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=" + influencerId + "&first=24");
            CloseableHttpResponse response = client.execute(getRequest);
            String content = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            String status = objectMapper.readTree(content).findValue("status").asText();
            if ("ok".equals(status)) {
                List<InstagramFeed> responses = objectMapper.readTree(content).findValues("node")
                        .stream()
                        .map(this::mapping)
                        .collect(Collectors.toList());

                instagramFeedRepo.saveAll(responses);
            } else {
                System.out.println("error");
                eventBus.emit(event);
            }


        } catch (Exception e) {
            System.out.println("errpr");
            eventBus.emit(event);
        }
    }


    InstagramFeed mapping(JsonNode edge) {
        try {
            InstagramFeed response = new InstagramFeed();
            response.setCode(edge.findValue("shortcode").asText());
            response.setLikeCount(edge.findValue("edge_media_preview_like").findValue("count").asInt(0));
            response.setCommentCount(edge.findValue("edge_media_to_comment").findValue("count").asInt(0));
            if (edge.findValue("text") != null) {
                response.setContent(edge.findValue("text").asText(""));
            }
            response.setInstagramUserId(edge.findValue("owner").findValue("id").asText());
            response.setThumbnailUrl(edge.findValue("thumbnail_src").asText());
            if (edge.findValue("video_view_count") != null) {
                response.setViewCount(edge.findValue("video_view_count").asInt(0));
            }
            response.setTakenAt(new Date(edge.findValue("taken_at_timestamp").asInt(0) * 1000L));
            response.setId(edge.findValue("id").asText() + "_" + response.getInstagramUserId());
            response.setCreatedDate(new Date());
            return response;
        } catch (Exception e) {
            return null;
        }
    }

}
