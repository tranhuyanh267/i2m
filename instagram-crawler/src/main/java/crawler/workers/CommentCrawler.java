package crawler.workers;

import crawler.messages.FeedIdMessage;
import crawler.repos.InstagramCommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaCommentsRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaCommentsResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class CommentCrawler {

    private Instagram4j instagram4j;
    private InstagramCommentRepository instagramCommentRepository;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "feed-id-queue")
    public void handle(FeedIdMessage message) {
        try {
            String feedId = message.getFeedId();
            InstagramGetMediaCommentsRequest request = new InstagramGetMediaCommentsRequest(feedId, null);
            InstagramGetMediaCommentsResult result = instagram4j.sendRequest(request);
            if (!"ok".equals(result.getStatus())) {
                rabbitTemplate.convertAndSend("feed-id-queue", message);
                return;
            }
            List<InstagramComment> comments = result.getComments();
            List<crawler.documents.InstagramComment> instagramComments = comments.stream().map(comment -> {
                crawler.documents.InstagramComment instagramComment = new crawler.documents.InstagramComment();
                instagramComment.setId(String.valueOf(comment.getPk()));
                instagramComment.setContent(comment.getText());
                instagramComment.setFeedId(feedId);
                return instagramComment;
            }).collect(Collectors.toList());
            instagramCommentRepository.saveAll(instagramComments);
        } catch (Exception ex) {
            rabbitTemplate.convertAndSend("feed-id-queue", message);
            log.error(ex.getMessage());
        }
    }

}
