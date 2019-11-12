package web.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Engagement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private float engagement;
    
    private String influencerId;

    private float averageLikePerPost;
    private float averageCommentPerPost;
    private float averageViewPerVideo;
    private float averageEngagementPerVideo;
    private float averageEngagementPerImage;

}
