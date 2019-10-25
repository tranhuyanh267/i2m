package calculation;

import calculation.documents.Influencer;
import calculation.handlers.Test;
import calculation.repos.InfluencerRepository;
import calculation.repos.PostRepository;
import common.payload.PostPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CalculationApplication implements ApplicationRunner {

    @Autowired
    PostRepository postRepository;

    @Autowired
    InfluencerRepository influencerRepository;

    @Autowired
    Test test;

    public static void main(String[] args) {
        SpringApplication.run(CalculationApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Influencer> all = influencerRepository.findAll();
        int count = 0;
        for (Influencer influencer : all) {
            test.calculate(influencer.getPk(), 396251);
            count++;
            System.out.println(count);
        }
    }

}
