package management.controllers;

import common.QueueName;
import lombok.AllArgsConstructor;
import management.documents.InfluencerUsername;
import management.repos.InfluencerUsernameRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import payloads.InstagramUserPayload;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class IndexController {

    private InfluencerUsernameRepo influencerUsernameRepo;
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public String index(Model model) {
        List<InfluencerUsername> influencerUsernames = influencerUsernameRepo.findAll();
        model.addAttribute("influencers", influencerUsernames);
        return "index";
    }

    @PostMapping
    public void trigger() {
        List<InfluencerUsername> influencers = influencerUsernameRepo.findAll();
        List<InstagramUserPayload> instagrams = influencers.stream().map(influencer -> {
            InstagramUserPayload instagramUserPayload = new InstagramUserPayload();
            instagramUserPayload.setUsername(influencer.getUsername());
            instagramUserPayload.setCategories(Collections.singletonList(influencer.getCategory()));
            return instagramUserPayload;
        }).collect(Collectors.toList());

        instagrams.forEach(i -> {
            rabbitTemplate.convertAndSend(QueueName.USER_NAME_QUEUE, i);
        });
    }
}
