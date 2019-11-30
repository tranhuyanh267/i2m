package crawler.workers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import crawler.documents.InstagramFeed;
import crawler.messages.FeedIdMessage;
import crawler.messages.UserIdMessage;
import crawler.repos.InstagramFeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class FeedCrawler {
    private InstagramFeedRepository instagramFeedRepository;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "user-id-queue")
    public void handler(UserIdMessage message) throws IOException {
        try {
            String influencerId = message.getUserId();

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet("https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=" + influencerId + "&first=50");
            CloseableHttpResponse response = client.execute(getRequest);
            String content = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            String status = objectMapper.readTree(content).findValue("status").asText();
            if ("ok".equals(status)) {
                List<JsonNode> nodes = objectMapper.readTree(content).findValues("node");

                List<InstagramFeed> responses = nodes
                        .stream()
                        .map(this::mapping)
                        .collect(Collectors.toList());

                instagramFeedRepository.saveAll(responses);

                nodes.stream()
                        .limit(12)
                        .map(this::mapping)
                        .forEach(feed -> {
                            FeedIdMessage feedIdMessage = new FeedIdMessage();
                            feedIdMessage.setFeedId(feed.getId());
                            rabbitTemplate.convertAndSend("feed-id-queue", feedIdMessage);
                        });

            } else {
                log.error(status);
                rabbitTemplate.convertAndSend("user-id-queue", message);
            }


        } catch (Exception ex) {
            log.error(ex.getMessage());
            rabbitTemplate.convertAndSend("user-id-queue", message);
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
