package web.handlers;

import common.QueueName;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import payloads.InstagramUserPayload;
import web.entities.Influencer;
import web.entities.Post;
import web.repositories.InfluencerRepository;
import web.repositories.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MessageHandler {

    private InfluencerRepository influencerRepository;
    private PostRepository postRepository;

    @RabbitListener(queues = QueueName.INFLUENCER_QUEUE)
    public void handleInfluencer(InstagramUserPayload payload) {
        Influencer influencer = new Influencer();
        BeanUtils.copyProperties(payload, influencer);
        influencer.setId(payload.getUserId());
        influencer.setPosts(null);
        influencerRepository.save(influencer);


        List<Post> posts = payload.getPosts().stream().map(post -> {
            Post p = new Post();
            BeanUtils.copyProperties(post, p);
            p.setInfluencer(influencer);
            return p;
        }).collect(Collectors.toList());

        postRepository.saveAll(posts);
    }

}

