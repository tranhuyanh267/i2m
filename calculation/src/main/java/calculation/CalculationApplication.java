package calculation;

import calculation.core.EventBus;
import calculation.documents.InstagramUser;
import calculation.repos.InstagramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@SpringBootApplication
public class CalculationApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(CalculationApplication.class, args);
    }

    @Autowired
    InstagramUserRepository instagramUserRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    EventBus eventBus;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<InstagramUser> instagramUsers = instagramUserRepository.findAll();
//        for (InstagramUser instagramUser : instagramUsers) {
//            eventBus.emit("instagram-user-queue", instagramUser);
//        }
    }
}

