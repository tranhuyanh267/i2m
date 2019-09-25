package worker.craw;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class PostCrawler {

    private Instagram4j instagram4j;


    public InstagramFeedResult crawPosts(long userId, String maxId) throws IOException {
        InstagramUserFeedRequest request = new InstagramUserFeedRequest(userId, maxId, 0, 0);
        InstagramFeedResult result = instagram4j.sendRequest(request);
        return result;
    }
}
