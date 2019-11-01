package calculation.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document
@Data
public class InstagramFeed {

    @Id
    private String id;
    private String code;
    private Date createdDate;
    private String instagramUserId;
    private boolean isVideo;
    @Field("like_count")
    private int likeCount;
    @Field("comment_count")
    private int commentCount;
    @Field("view_count")
    private int viewCount;
    private float engagement;
    @Field("caption.text")
    private String text;
    @Field("taken_at")
    private int takenAt;

    @Field("image_versions2.candidates")
    private List<Candidate> imageVersion2;

    @Field("carousel_media")
    private List<Carousel> carousels;

    public String getThumbnailUrl() {
        try {
            if (imageVersion2 != null) {
                return imageVersion2.get(0).getUrl();
            }
            return carousels.get(0).getCandidates().get(0).getUrl();
        } catch (Exception ex) {
            return "";
        }
    }

    public Date getTakenAt() {
        return new Date(this.takenAt * 1000L);
    }

    public boolean isVideo() {
        return this.viewCount > 0;
    }

}

