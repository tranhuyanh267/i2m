package crawler.entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Influencer {
    @Id
    private String id;
    private String fullName;
    private String username;
    private String biography;
    private int followers;
    private int followings;
    private int mediaCount;
    private float engagement;
    private String profilePicUrl;
    private String email;
    private boolean isVerified;
    private String externalUrl;
}

