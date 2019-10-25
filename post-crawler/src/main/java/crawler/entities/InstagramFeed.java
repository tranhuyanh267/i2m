package crawler.entities;

import lombok.Data;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class InstagramFeed extends InstagramFeedItem {

    @Id
    private String id;
    private Date createdDate;
    private String instagramUserId;

    public InstagramFeed(InstagramFeedItem item) {
        BeanUtils.copyProperties(item, this);
        this.id = item.getId();
        this.createdDate = new Date();
        this.instagramUserId = String.valueOf(item.getUser().getPk());
    }
}
