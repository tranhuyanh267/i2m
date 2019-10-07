package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import web.config.AppProperties;
import web.repositories.InfluencerRepository;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class WebAppApplication {
    @Autowired
    InfluencerRepository influencerService;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

}
